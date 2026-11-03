import { Check } from 'lucide-react';
import { CHANNEL_OPTIONS } from '../../data/notificationChannels';
import './ChannelSelector.css';

/**
 * Radio-card group for the user's NotificationStrategy.
 * activeValue (optional) marks the channel currently saved on the server.
 */
export default function ChannelSelector({
  name,
  value,
  onChange,
  activeValue,
  compact = false,
  disabled = false,
  legend,
  error,
}) {
  return (
    <fieldset className={`tk-channels ${compact ? 'tk-channels--compact' : ''}`} disabled={disabled}>
      {legend && <legend className="tk-channels__legend">{legend}</legend>}
      <div className="tk-channels__grid">
        {CHANNEL_OPTIONS.map(({ value: optionValue, title, icon: Icon, description }) => {
          const checked = value === optionValue;
          return (
            <label
              key={optionValue}
              className={`tk-channel ${checked ? 'tk-channel--checked' : ''}`}
            >
              <input
                type="radio"
                name={name}
                value={optionValue}
                checked={checked}
                onChange={() => onChange(optionValue)}
                className="tk-channel__radio"
              />
              <span className="tk-channel__icon" aria-hidden="true">
                <Icon size={compact ? 16 : 18} />
              </span>
              <span className="tk-channel__text">
                <span className="tk-channel__title-row">
                  <span className="tk-channel__title">{title}</span>
                  {activeValue === optionValue && <span className="tk-channel__badge">Active</span>}
                </span>
                {!compact && <span className="tk-channel__desc">{description}</span>}
              </span>
              <span className="tk-channel__check" aria-hidden="true">
                <Check size={12} strokeWidth={3} />
              </span>
            </label>
          );
        })}
      </div>
      {error && (
        <p className="tk-channels__error" role="alert">
          {error}
        </p>
      )}
    </fieldset>
  );
}
