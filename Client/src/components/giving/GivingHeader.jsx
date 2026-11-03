import { Heart, Clock } from 'lucide-react';
import './GivingHeader.css';

export default function GivingHeader({ activeTab, onTabChange, historyCount = 0 }) {
  return (
    <div className="tk-giving-header">
      <div className="tk-giving-header__titles">
        <h1 className="tk-giving-header__title">Giving Hub</h1>
        <p className="tk-giving-header__subtitle">
          Support local families directly with funds, warm meals, or emergency supplies.
        </p>
      </div>

      {/* Primary Tab Switcher */}
      <div className="tk-giving-header__tabs" role="tablist">
        <button
          type="button"
          role="tab"
          aria-selected={activeTab === 'DONATE'}
          className={`tk-giving-header__tab ${activeTab === 'DONATE' ? 'tk-giving-header__tab--active' : ''}`}
          onClick={() => onTabChange('DONATE')}
        >
          <Heart size={16} />
          <span>Make a Donation</span>
        </button>

        <button
          type="button"
          role="tab"
          aria-selected={activeTab === 'HISTORY'}
          className={`tk-giving-header__tab ${activeTab === 'HISTORY' ? 'tk-giving-header__tab--active' : ''}`}
          onClick={() => onTabChange('HISTORY')}
        >
          <Clock size={16} />
          <span>My Donation History</span>
          {historyCount > 0 && (
            <span className="tk-giving-header__count-badge">{historyCount}</span>
          )}
        </button>
      </div>
    </div>
  );
}
