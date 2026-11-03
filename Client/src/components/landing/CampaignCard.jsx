import { useNavigate } from 'react-router-dom';
import { MapPin, Calendar } from 'lucide-react';
import Card from '../common/Card';
import Badge from '../common/Badge';
import Button from '../common/Button';
import './CampaignCard.css';

export default function CampaignCard({ campaign }) {
  const navigate = useNavigate();

  if (!campaign) {
    return null;
  }

  const isFundraiser = campaign.type === 'FUNDRAISER';
  const progressPct = campaign.target
    ? Math.min(100, Math.round((campaign.raised / campaign.target) * 100))
    : campaign.capacity
    ? Math.min(100, Math.round((campaign.enrolled / campaign.capacity) * 100))
    : 0;

  return (
    <Card elevation="sm" padding="none" hover className="tk-campaign-card">
      {/* Optional image or themed visual bar */}
      <div className="tk-campaign-card__cover">
        <div className="tk-campaign-card__cover-tint" />
        <div className="tk-campaign-card__badges">
          {campaign.badge && (
            <Badge variant={campaign.badgeVariant || 'critical'} size="sm">
              {campaign.badge}
            </Badge>
          )}
          {campaign.code && (
            <Badge variant="default" size="sm">
              {campaign.code}
            </Badge>
          )}
        </div>
      </div>

      <div className="tk-campaign-card__body">
        {/* Meta / Location or Time */}
        <div className="tk-campaign-card__meta">
          {isFundraiser ? (
            <div className="tk-campaign-card__meta-item">
              <MapPin size={14} />
              <span>{campaign.location || 'Central Depot'}</span>
            </div>
          ) : (
            <div className="tk-campaign-card__meta-item">
              <Calendar size={14} />
              <span>{campaign.schedule || 'Scheduled Event'}</span>
            </div>
          )}
        </div>

        <h4 className="tk-campaign-card__title">{campaign.title}</h4>
        <p className="tk-campaign-card__desc">{campaign.description}</p>

        {/* Financial / Capacity Metrics */}
        {isFundraiser ? (
          <div className="tk-campaign-card__stats-block">
            <div className="tk-campaign-card__sums">
              <span className="tk-campaign-card__raised">${campaign.raised?.toLocaleString()}</span>
              <span className="tk-campaign-card__target">
                of ${campaign.target?.toLocaleString()} target
              </span>
            </div>

            <div className="tk-campaign-card__progress-track">
              <div
                className="tk-campaign-card__progress-fill"
                style={{ width: `${progressPct}%` }}
              />
            </div>

            <div className="tk-campaign-card__details-row">
              <span>{progressPct}% Funded</span>
              <span>{campaign.patronsCount || 0} Patrons Active</span>
            </div>
          </div>
        ) : (
          <div className="tk-campaign-card__stats-block">
            <div className="tk-campaign-card__capacity-row">
              <span className="tk-campaign-card__capacity-label">Workshop Registration</span>
              <span className="tk-campaign-card__capacity-val">
                {campaign.enrolled || 0}/{campaign.capacity || 0} spots filled
              </span>
            </div>

            <div className="tk-campaign-card__progress-track tk-campaign-card__progress-track--teal">
              <div
                className="tk-campaign-card__progress-fill tk-campaign-card__progress-fill--teal"
                style={{ width: `${progressPct}%` }}
              />
            </div>

            <div className="tk-campaign-card__details-row">
              <span>{campaign.timeRemaining || 'Spots Available'}</span>
              <span>{campaign.priceLabel || 'Free for all'}</span>
            </div>
          </div>
        )}

        {/* Footer CTA */}
        <div className="tk-campaign-card__footer">
          <span className="tk-campaign-card__sub-info">
            {campaign.subInfo || (isFundraiser ? 'Verified Direct Aid' : 'No ID Required')}
          </span>

          <Button
            variant={isFundraiser ? 'primary' : 'outline'}
            size="sm"
            onClick={() => {
              if (isFundraiser) {
                navigate('/giving-hub', { state: { campaignId: campaign.id } });
              } else {
                navigate('/auth');
              }
            }}
          >
            {isFundraiser ? 'Donate Now' : 'Reserve Ticket'}
          </Button>
        </div>
      </div>
    </Card>
  );
}
