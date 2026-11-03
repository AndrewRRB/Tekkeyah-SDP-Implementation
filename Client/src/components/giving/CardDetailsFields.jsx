import { CreditCard, Lock, User } from 'lucide-react';
import { cardBrand, cvvLength, formatCardNumber, formatExpiry } from '../../utils/card';
import './CardDetailsFields.css';

/**
 * Card number / name / expiry / CVV for a CARD donation.
 * value: { holderName, number, expiry, cvv } (display strings), errors: { field: message }
 */
export default function CardDetailsFields({ value, onChange, errors = {} }) {
  const brand = cardBrand(value.number);
  const set = (field, next) => onChange({ ...value, [field]: next });

  const fieldError = (field) =>
    errors[field] && (
      <p id={`card-${field}-error`} className="tk-card-fields__error" role="alert">
        {errors[field]}
      </p>
    );

  const describedBy = (field) => (errors[field] ? `card-${field}-error` : undefined);

  return (
    <div className="tk-card-fields">
      <div className="tk-card-fields__row tk-card-fields__row--full">
        <label className="tk-card-fields__label" htmlFor="card-number">
          Card Number
        </label>
        <div className={`tk-card-fields__input-wrap ${errors.number ? 'tk-card-fields__input-wrap--invalid' : ''}`}>
          <CreditCard size={17} className="tk-card-fields__icon" aria-hidden="true" />
          <input
            id="card-number"
            type="text"
            inputMode="numeric"
            autoComplete="cc-number"
            placeholder="1234 5678 9012 3456"
            value={value.number}
            onChange={(e) => set('number', formatCardNumber(e.target.value))}
            aria-invalid={Boolean(errors.number)}
            aria-describedby={describedBy('number')}
            className="tk-card-fields__input tk-card-fields__input--mono"
          />
          {brand && <span className="tk-card-fields__brand">{brand}</span>}
        </div>
        {fieldError('number')}
      </div>

      <div className="tk-card-fields__row tk-card-fields__row--full">
        <label className="tk-card-fields__label" htmlFor="card-name">
          Name on Card
        </label>
        <div className={`tk-card-fields__input-wrap ${errors.holderName ? 'tk-card-fields__input-wrap--invalid' : ''}`}>
          <User size={17} className="tk-card-fields__icon" aria-hidden="true" />
          <input
            id="card-name"
            type="text"
            autoComplete="cc-name"
            placeholder="As printed on the card"
            value={value.holderName}
            onChange={(e) => set('holderName', e.target.value)}
            aria-invalid={Boolean(errors.holderName)}
            aria-describedby={describedBy('holderName')}
            className="tk-card-fields__input"
          />
        </div>
        {fieldError('holderName')}
      </div>

      <div className="tk-card-fields__row">
        <label className="tk-card-fields__label" htmlFor="card-expiry">
          Expiry
        </label>
        <div className={`tk-card-fields__input-wrap ${errors.expiry ? 'tk-card-fields__input-wrap--invalid' : ''}`}>
          <input
            id="card-expiry"
            type="text"
            inputMode="numeric"
            autoComplete="cc-exp"
            placeholder="MM / YY"
            value={value.expiry}
            onChange={(e) => set('expiry', formatExpiry(e.target.value))}
            aria-invalid={Boolean(errors.expiry)}
            aria-describedby={describedBy('expiry')}
            className="tk-card-fields__input tk-card-fields__input--plain tk-card-fields__input--mono"
          />
        </div>
        {fieldError('expiry')}
      </div>

      <div className="tk-card-fields__row">
        <label className="tk-card-fields__label" htmlFor="card-cvv">
          Security Code
        </label>
        <div className={`tk-card-fields__input-wrap ${errors.cvv ? 'tk-card-fields__input-wrap--invalid' : ''}`}>
          <Lock size={15} className="tk-card-fields__icon" aria-hidden="true" />
          <input
            id="card-cvv"
            type="password"
            inputMode="numeric"
            autoComplete="cc-csc"
            placeholder={'•'.repeat(cvvLength(value.number))}
            maxLength={cvvLength(value.number)}
            value={value.cvv}
            onChange={(e) => set('cvv', e.target.value.replace(/\D/g, ''))}
            aria-invalid={Boolean(errors.cvv)}
            aria-describedby={describedBy('cvv')}
            className="tk-card-fields__input tk-card-fields__input--mono"
          />
        </div>
        {fieldError('cvv')}
      </div>

      <p className="tk-card-fields__note">
        <Lock size={13} aria-hidden="true" />
        <span>Only the card brand and last four digits are kept. The full number and code are never stored.</span>
      </p>
    </div>
  );
}
