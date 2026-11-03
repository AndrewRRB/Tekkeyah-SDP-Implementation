/**
 * Card rules per brand. The donor picks the brand; the number must be digits
 * only and have one of that brand's lengths.
 * Mirrors Server/src/main/java/com/tekkeyah/model/enums/CardBrand.java.
 */
export const CARD_BRANDS = {
  VISA: {
    label: 'Visa',
    lengths: [13, 16, 19],
    cvvLength: 3,
    groups: [4, 4, 4, 4, 3],
  },
  MASTERCARD: {
    label: 'Mastercard',
    lengths: [16],
    cvvLength: 3,
    groups: [4, 4, 4, 4],
  },
  AMEX: {
    label: 'American Express',
    lengths: [15],
    cvvLength: 4,
    groups: [4, 6, 5],
  },
};

const digitsOnly = (value) => value.replace(/\D/g, '');

export const maxCardDigits = (brand) => Math.max(...CARD_BRANDS[brand].lengths);

export const cvvLength = (brand) => CARD_BRANDS[brand].cvvLength;

const lengthsText = (lengths) =>
  lengths.length === 1 ? `${lengths[0]}` : `${lengths.slice(0, -1).join(', ')} or ${lengths.at(-1)}`;

/** Groups digits the way the chosen brand prints them, capped at its max length. */
export const formatCardNumber = (value, brand) => {
  const digits = digitsOnly(value).slice(0, maxCardDigits(brand));
  const parts = [];
  let index = 0;
  for (const size of CARD_BRANDS[brand].groups) {
    if (index >= digits.length) break;
    parts.push(digits.slice(index, index + size));
    index += size;
  }
  return parts.join(' ');
};

/** "1228" -> "12 / 28" */
export const formatExpiry = (value) => {
  const digits = digitsOnly(value).slice(0, 4);
  return digits.length > 2 ? `${digits.slice(0, 2)} / ${digits.slice(2)}` : digits;
};

/** Message for the number field, or null when it is valid for the chosen brand. */
const numberError = (number, brand) => {
  const { label, lengths } = CARD_BRANDS[brand];
  const digits = digitsOnly(number);
  if (!lengths.includes(digits.length)) {
    return `${label} numbers have ${lengthsText(lengths)} digits (you entered ${digits.length}).`;
  }
  return null;
};

/** Returns { field: message } for every invalid field; empty object when valid. */
export const validateCard = ({ brand, holderName, number, expiry, cvv }, today = new Date()) => {
  const errors = {};
  if (holderName.trim().length < 2) errors.holderName = 'Enter the name on the card.';

  const numberMessage = numberError(number, brand);
  if (numberMessage) errors.number = numberMessage;

  const [month, year] = digitsOnly(expiry).match(/^(\d{2})(\d{2})$/)?.slice(1).map(Number) ?? [];
  if (!month || month < 1 || month > 12) {
    errors.expiry = 'Use MM / YY.';
  } else {
    const firstDayAfterExpiry = new Date(2000 + year, month, 1);
    if (firstDayAfterExpiry <= new Date(today.getFullYear(), today.getMonth(), 1)) {
      errors.expiry = 'This card has expired.';
    }
  }

  if (!new RegExp(`^\\d{${cvvLength(brand)}}$`).test(cvv)) {
    errors.cvv = `${cvvLength(brand)} digits.`;
  }
  return errors;
};

/** Shape the server expects (CardDetails record). */
export const toCardPayload = ({ brand, holderName, number, expiry, cvv }) => {
  const [month, year] = digitsOnly(expiry).match(/^(\d{2})(\d{2})$/).slice(1).map(Number);
  return {
    brand,
    holderName: holderName.trim(),
    number: digitsOnly(number),
    expiryMonth: month,
    expiryYear: 2000 + year,
    cvv,
  };
};
