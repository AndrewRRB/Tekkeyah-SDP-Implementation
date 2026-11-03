/** Client-side card checks; the server repeats every one of them. */

const digitsOnly = (value) => value.replace(/\D/g, '');

export const cardBrand = (number) => {
  const digits = digitsOnly(number);
  if (/^4/.test(digits)) return 'Visa';
  if (/^3[47]/.test(digits)) return 'American Express';
  const first4 = Number(digits.slice(0, 4));
  if (/^5[1-5]/.test(digits) || (first4 >= 2221 && first4 <= 2720)) return 'Mastercard';
  return null;
};

const passesLuhn = (digits) => {
  let sum = 0;
  let doubleIt = false;
  for (let i = digits.length - 1; i >= 0; i -= 1) {
    let d = Number(digits[i]);
    if (doubleIt) {
      d *= 2;
      if (d > 9) d -= 9;
    }
    sum += d;
    doubleIt = !doubleIt;
  }
  return sum % 10 === 0;
};

/** "4242424242424242" -> "4242 4242 4242 4242" (Amex: 4-6-5). */
export const formatCardNumber = (value) => {
  const digits = digitsOnly(value).slice(0, 19);
  if (cardBrand(digits) === 'American Express') {
    return [digits.slice(0, 4), digits.slice(4, 10), digits.slice(10, 15)].filter(Boolean).join(' ');
  }
  return digits.replace(/(\d{4})(?=\d)/g, '$1 ');
};

/** "1228" -> "12 / 28" */
export const formatExpiry = (value) => {
  const digits = digitsOnly(value).slice(0, 4);
  return digits.length > 2 ? `${digits.slice(0, 2)} / ${digits.slice(2)}` : digits;
};

export const cvvLength = (number) => (cardBrand(number) === 'American Express' ? 4 : 3);

/** Returns { field: message } for every invalid field; empty object when valid. */
export const validateCard = ({ holderName, number, expiry, cvv }, today = new Date()) => {
  const errors = {};
  if (holderName.trim().length < 2) errors.holderName = 'Enter the name on the card.';

  const digits = digitsOnly(number);
  if (digits.length < 13 || digits.length > 19 || !passesLuhn(digits)) {
    errors.number = 'Card number is not valid.';
  }

  const [month, year] = digitsOnly(expiry).match(/^(\d{2})(\d{2})$/)?.slice(1).map(Number) ?? [];
  if (!month || month < 1 || month > 12) {
    errors.expiry = 'Use MM / YY.';
  } else {
    const endOfExpiryMonth = new Date(2000 + year, month, 1);
    if (endOfExpiryMonth <= new Date(today.getFullYear(), today.getMonth(), 1)) {
      errors.expiry = 'This card has expired.';
    }
  }

  if (!new RegExp(`^\\d{${cvvLength(number)}}$`).test(cvv)) {
    errors.cvv = `${cvvLength(number)} digits.`;
  }
  return errors;
};

/** Shape the server expects (CardDetails record). */
export const toCardPayload = ({ holderName, number, expiry, cvv }) => {
  const [month, year] = digitsOnly(expiry).match(/^(\d{2})(\d{2})$/).slice(1).map(Number);
  return {
    holderName: holderName.trim(),
    number: digitsOnly(number),
    expiryMonth: month,
    expiryYear: 2000 + year,
    cvv,
  };
};
