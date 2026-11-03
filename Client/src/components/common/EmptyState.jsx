import { Inbox } from 'lucide-react';
import './EmptyState.css';

export default function EmptyState({
  title = 'No data exists',
  description = 'There are currently no records or items to display.',
  icon: Icon = Inbox,
  action,
  className = '',
}) {
  return (
    <div className={`tk-empty-state ${className}`}>
      <div className="tk-empty-state__icon-wrap">
        <Icon className="tk-empty-state__icon" />
      </div>
      <h4 className="tk-empty-state__title">{title}</h4>
      {description && <p className="tk-empty-state__desc">{description}</p>}
      {action && <div className="tk-empty-state__action">{action}</div>}
    </div>
  );
}
