import './Badge.css';

export default function Badge({
  children,
  variant = 'default',
  size = 'md',
  icon,
  className = '',
}) {
  return (
    <span className={`tk-badge tk-badge--${variant} tk-badge--${size} ${className}`}>
      {icon && <span className="tk-badge__icon">{icon}</span>}
      <span className="tk-badge__content">{children}</span>
    </span>
  );
}
