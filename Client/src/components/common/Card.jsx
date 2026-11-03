import './Card.css';

export default function Card({
  children,
  className = '',
  elevation = 'sm',
  padding = 'md',
  hover = false,
  bordered = true,
  onClick,
  ...props
}) {
  return (
    <div
      className={`tk-card tk-card--elevation-${elevation} tk-card--padding-${padding} ${bordered ? 'tk-card--bordered' : ''} ${hover ? 'tk-card--hover' : ''} ${className}`}
      onClick={onClick}
      {...props}
    >
      {children}
    </div>
  );
}
