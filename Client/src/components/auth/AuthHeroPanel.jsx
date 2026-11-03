import { HeartHandshake, ShieldCheck, Heart, Users, Sparkles } from 'lucide-react';
import Badge from '../common/Badge';
import './AuthHeroPanel.css';

export default function AuthHeroPanel() {
  return (
    <div className="tk-auth-hero">
      <div className="tk-auth-hero__overlay" />

      {/* Header */}
      <div className="tk-auth-hero__header">
        <div className="tk-auth-hero__brand">
          <div className="tk-auth-hero__logo">
            <HeartHandshake size={24} />
          </div>
          <div>
            <span className="tk-auth-hero__title">Tekkeyah</span>
            <span className="tk-auth-hero__sub">COMMUNITY RELIEF & AID</span>
          </div>
        </div>
        <Badge variant="dark" size="sm" icon={<span className="tk-auth-hero__green-dot" />}>
          Verified Foundation
        </Badge>
      </div>

      {/* Main Pitch */}
      <div className="tk-auth-hero__content">
        <div className="tk-auth-hero__tag">
          <Badge variant="dark" size="md" icon={<ShieldCheck size={14} />}>
            Centuries of Mutual Aid & Dignity
          </Badge>
        </div>

        <blockquote className="tk-auth-hero__quote">
          “Every meal shared and every shift claimed builds the fabric of our city.”
        </blockquote>

        <p className="tk-auth-hero__description">
          Rooted in the timeless tradition of the civic Tekkeyah, we orchestrate zero-waste
          community kitchens, direct emergency relief logistics, and radical transparency
          for all civic contributors.
        </p>

        {/* Real-time stats */}
        <div className="tk-auth-hero__stats">
          <div className="tk-auth-hero__stat-col">
            <span className="tk-auth-hero__stat-label">MEALS SERVED</span>
            <span className="tk-auth-hero__stat-val">142,850+</span>
            <span className="tk-auth-hero__stat-note">100% Verified Distributed</span>
          </div>
          <div className="tk-auth-hero__stat-col">
            <span className="tk-auth-hero__stat-label">VOLUNTEERS</span>
            <span className="tk-auth-hero__stat-val">3,420</span>
            <span className="tk-auth-hero__stat-note">Across 14 City Hubs</span>
          </div>
        </div>
      </div>

      {/* Footer Values */}
      <div className="tk-auth-hero__footer">
        <div className="tk-auth-hero__value-pill">
          <Sparkles size={14} />
          <span>Radical Transparency</span>
        </div>
        <div className="tk-auth-hero__value-pill">
          <Heart size={14} />
          <span>Dignity-First Standard</span>
        </div>
        <div className="tk-auth-hero__value-pill">
          <Users size={14} />
          <span>Community Owned</span>
        </div>
      </div>
    </div>
  );
}
