/**
 * Supported phone countries. minDigits / maxDigits count the national number
 * WITHOUT the leading trunk "0". Keep in sync with the server's
 * Server/src/main/java/com/tekkeyah/util/PhoneNumbers.java.
 */
export const PHONE_COUNTRIES = [
  { iso: 'EG', name: 'Egypt', dialCode: '+20', minDigits: 10, maxDigits: 10, example: '10 1234 5678' },
  { iso: 'SA', name: 'Saudi Arabia', dialCode: '+966', minDigits: 9, maxDigits: 9, example: '50 123 4567' },
  { iso: 'AE', name: 'United Arab Emirates', dialCode: '+971', minDigits: 9, maxDigits: 9, example: '50 123 4567' },
  { iso: 'JO', name: 'Jordan', dialCode: '+962', minDigits: 9, maxDigits: 9, example: '7 9123 4567' },
  { iso: 'KW', name: 'Kuwait', dialCode: '+965', minDigits: 8, maxDigits: 8, example: '5001 2345' },
  { iso: 'QA', name: 'Qatar', dialCode: '+974', minDigits: 8, maxDigits: 8, example: '3312 3456' },
  { iso: 'BH', name: 'Bahrain', dialCode: '+973', minDigits: 8, maxDigits: 8, example: '3600 1234' },
  { iso: 'OM', name: 'Oman', dialCode: '+968', minDigits: 8, maxDigits: 8, example: '9212 3456' },
  { iso: 'LB', name: 'Lebanon', dialCode: '+961', minDigits: 7, maxDigits: 8, example: '71 123 456' },
  { iso: 'IQ', name: 'Iraq', dialCode: '+964', minDigits: 10, maxDigits: 10, example: '791 234 5678' },
  { iso: 'PS', name: 'Palestine', dialCode: '+970', minDigits: 9, maxDigits: 9, example: '599 123 456' },
  { iso: 'MA', name: 'Morocco', dialCode: '+212', minDigits: 9, maxDigits: 9, example: '650 123 456' },
  { iso: 'TN', name: 'Tunisia', dialCode: '+216', minDigits: 8, maxDigits: 8, example: '20 123 456' },
  { iso: 'TR', name: 'Turkey', dialCode: '+90', minDigits: 10, maxDigits: 10, example: '501 234 5678' },
  { iso: 'GB', name: 'United Kingdom', dialCode: '+44', minDigits: 10, maxDigits: 10, example: '7400 123456' },
  { iso: 'DE', name: 'Germany', dialCode: '+49', minDigits: 10, maxDigits: 11, example: '1512 3456789' },
  { iso: 'FR', name: 'France', dialCode: '+33', minDigits: 9, maxDigits: 9, example: '6 12 34 56 78' },
  { iso: 'US', name: 'United States / Canada', dialCode: '+1', minDigits: 10, maxDigits: 10, example: '201 555 0123' },
];

export const DEFAULT_PHONE_COUNTRY = PHONE_COUNTRIES[0];
