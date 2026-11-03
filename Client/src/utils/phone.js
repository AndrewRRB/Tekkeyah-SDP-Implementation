import { DEFAULT_PHONE_COUNTRY, PHONE_COUNTRIES } from '../data/phoneCountries';

export const findCountry = (dialCode) =>
  PHONE_COUNTRIES.find((country) => country.dialCode === dialCode) ?? DEFAULT_PHONE_COUNTRY;

/** Digits the person typed, without separators or the trunk "0". */
const nationalDigits = (number) => {
  const digits = number.replace(/\D/g, '');
  return digits.startsWith('0') ? digits.slice(1) : digits;
};

/** Error message for the field, or null when the number is valid. Mirrors the server rule. */
export const validatePhone = ({ dialCode, number }) => {
  const country = findCountry(dialCode);
  if (!number.trim()) return 'Phone number is required.';
  if (/[^\d\s\-().]/.test(number)) return 'Use digits only.';
  const length = nationalDigits(number).length;
  if (length < country.minDigits || length > country.maxDigits) {
    const expected =
      country.minDigits === country.maxDigits
        ? `${country.minDigits}`
        : `${country.minDigits}–${country.maxDigits}`;
    return `${country.name} numbers have ${expected} digits after ${country.dialCode} (you entered ${length}).`;
  }
  return null;
};

/** "+201012345678" -> { dialCode: '+20', number: '1012345678' } (longest dial code wins). */
export const splitE164 = (e164) => {
  if (!e164) return { dialCode: DEFAULT_PHONE_COUNTRY.dialCode, number: '' };
  const match = [...PHONE_COUNTRIES]
    .sort((a, b) => b.dialCode.length - a.dialCode.length)
    .find((country) => e164.startsWith(country.dialCode));
  return match
    ? { dialCode: match.dialCode, number: e164.slice(match.dialCode.length) }
    : { dialCode: DEFAULT_PHONE_COUNTRY.dialCode, number: e164.replace(/^\+/, '') };
};
