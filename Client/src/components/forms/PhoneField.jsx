import { useId } from 'react';
import { ChevronDown, Phone } from 'lucide-react';
import { PHONE_COUNTRIES } from '../../data/phoneCountries';
import { findCountry } from '../../utils/phone';
import './PhoneField.css';

/**
 * Country dial-code dropdown + national number input.
 * value: { dialCode: '+20', number: '010 1234 5678' }
 */
export default function PhoneField({ label = 'Phone Number', value, onChange, error, required = true }) {
  const inputId = useId();
  const errorId = `${inputId}-error`;
  const country = findCountry(value.dialCode);

  return (
    <div className="tk-phone-field">
      <label className="tk-phone-field__label" htmlFor={inputId}>
        {label}
      </label>
      <div className={`tk-phone-field__control ${error ? 'tk-phone-field__control--invalid' : ''}`}>
        <div className="tk-phone-field__country">
          <select
            aria-label="Country code"
            className="tk-phone-field__select"
            value={value.dialCode}
            onChange={(e) => onChange({ ...value, dialCode: e.target.value })}
          >
            {PHONE_COUNTRIES.map((c) => (
              <option key={c.iso} value={c.dialCode}>
                {c.iso} {c.dialCode} · {c.name}
              </option>
            ))}
          </select>
          <span className="tk-phone-field__country-display" aria-hidden="true">
            <span className="tk-phone-field__iso">{country.iso}</span>
            <span className="tk-phone-field__dial">{country.dialCode}</span>
            <ChevronDown size={14} />
          </span>
        </div>
        <div className="tk-phone-field__number">
          <Phone size={16} className="tk-phone-field__icon" aria-hidden="true" />
          <input
            id={inputId}
            type="tel"
            inputMode="tel"
            autoComplete="tel-national"
            required={required}
            placeholder={country.example}
            value={value.number}
            onChange={(e) => onChange({ ...value, number: e.target.value })}
            aria-invalid={Boolean(error)}
            aria-describedby={error ? errorId : undefined}
            className="tk-phone-field__input"
          />
        </div>
      </div>
      {error && (
        <p id={errorId} className="tk-phone-field__error" role="alert">
          {error}
        </p>
      )}
    </div>
  );
}
