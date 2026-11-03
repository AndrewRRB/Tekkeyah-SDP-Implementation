import { useState } from 'react';
import { Sparkles } from 'lucide-react';
import Badge from '../common/Badge';
import EmptyState from '../common/EmptyState';
import CampaignCard from './CampaignCard';
import { DEFAULT_CAMPAIGNS } from '../../data/campaignPresets';
import './CampaignsSection.css';

export default function CampaignsSection({ campaigns = DEFAULT_CAMPAIGNS }) {
  const [activeFilter, setActiveFilter] = useState('ALL');

  const filtered = campaigns.filter((item) => {
    if (activeFilter === 'EMERGENCY') return item.category === 'EMERGENCY';
    if (activeFilter === 'OUTREACH') return item.category === 'OUTREACH';
    return true;
  });

  return (
    <section className="tk-campaigns-section" id="events">
      <div className="tk-campaigns-container">
        {/* Header */}
        <div className="tk-campaigns-header">
          <div>
            <div className="tk-campaigns-badge">
              <Badge variant="default" size="sm" icon={<Sparkles size={12} />}>
                DIRECT COMMUNITY ACTION
              </Badge>
            </div>
            <h2 className="tk-campaigns-title">Featured Campaigns & Local Outreaches</h2>
            <p className="tk-campaigns-subtitle">
              Choose how you make an immediate, tangible impact. Real-time updates with verified balance sheets.
            </p>
          </div>

          {/* Filter Tabs */}
          <div className="tk-campaigns-filters" role="tablist">
            <button
              type="button"
              role="tab"
              aria-selected={activeFilter === 'ALL'}
              className={`tk-campaigns-filter-btn ${activeFilter === 'ALL' ? 'tk-campaigns-filter-btn--active' : ''}`}
              onClick={() => setActiveFilter('ALL')}
            >
              All Initiatives
            </button>
            <button
              type="button"
              role="tab"
              aria-selected={activeFilter === 'EMERGENCY'}
              className={`tk-campaigns-filter-btn ${activeFilter === 'EMERGENCY' ? 'tk-campaigns-filter-btn--active' : ''}`}
              onClick={() => setActiveFilter('EMERGENCY')}
            >
              Emergency Drives
            </button>
            <button
              type="button"
              role="tab"
              aria-selected={activeFilter === 'OUTREACH'}
              className={`tk-campaigns-filter-btn ${activeFilter === 'OUTREACH' ? 'tk-campaigns-filter-btn--active' : ''}`}
              onClick={() => setActiveFilter('OUTREACH')}
            >
              Outreach Days
            </button>
          </div>
        </div>

        {/* Content / Grid or Empty State */}
        {filtered.length > 0 ? (
          <div className="tk-campaigns-grid">
            {filtered.map((item) => (
              <CampaignCard key={item.id} campaign={item} />
            ))}
          </div>
        ) : (
          <EmptyState
            title="No data exists"
            description="There are currently no active initiatives matching this filter."
          />
        )}
      </div>
    </section>
  );
}
