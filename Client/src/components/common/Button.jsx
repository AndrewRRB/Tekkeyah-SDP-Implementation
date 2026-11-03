import './Button.css';

export default function Button({
  children,
  variant = 'primary',
  size = 'md',
  iconLeft,
  iconRight,
  isLoading = false,
  fullWidth = false,
  className = '',
  disabled,
  type = 'button',
  onClick,
  ...props
}) {
  return (
    <button
      type={type}
      className={`tk-button tk-button--${variant} tk-button--${size} ${fullWidth ? 'tk-button--full' : ''} ${className}`}
      disabled={disabled || isLoading}
      onClick={onClick}
      {...props}
    >
      {isLoading ? (
        <span className="tk-button__spinner" aria-hidden="true" />
      ) : (
        <>
          {iconLeft && <span className="tk-button__icon">{iconLeft}</span>}
          <span className="tk-button__label">{children}</span>
          {iconRight && <span className="tk-button__icon">{iconRight}</span>}
        </>
      )}
    </button>
  );
}
