import { ShieldCheck, Eye, Lock, Users } from 'lucide-react';
import Card from '../common/Card';
import './CommunityCharter.css';

export default function CommunityCharter() {
  return (
    <section className="tk-charter-section" id="charter">
      <div className="tk-charter-container">
        <div className="tk-charter-layout">
          {/* Left: Manifesto Quote Card */}
          <div className="tk-charter-manifesto">
            <span className="tk-charter-tag">THE COMMUNITY CHARTER</span>

            <blockquote className="tk-charter-quote">
              “We do not offer charity from above; we build <mark>shared tables from within</mark>.
              In the heritage of Tekkeyah, bread and warmth are <mark>not privileges to be earned,
              but human birthrights to be guarded</mark>.”
            </blockquote>

            <div className="tk-charter-standard">
              <p className="tk-charter-standard-note">
                Rooted in unconditional community mutual aid.
              </p>
            </div>
          </div>

          {/* Right: 4 Governance Pillars */}
          <div className="tk-charter-pillars">
            <Card elevation="sm" padding="md" className="tk-charter-pillar">
              <div className="tk-charter-pillar__icon tk-charter-pillar__icon--green">
                <ShieldCheck size={20} />
              </div>
              <h4 className="tk-charter-pillar__title">Zero Overhead Leak</h4>
              <p className="tk-charter-pillar__desc">
                100% of community contributions go directly to food, winter heating, and shelter supplies.
              </p>
            </Card>

            <Card elevation="sm" padding="md" className="tk-charter-pillar">
              <div className="tk-charter-pillar__icon tk-charter-pillar__icon--teal">
                <Eye size={20} />
              </div>
              <h4 className="tk-charter-pillar__title">Open Disclosures</h4>
              <p className="tk-charter-pillar__desc">
                Grocery invoices and distribution records are transparent and accessible to every contributor.
              </p>
            </Card>

            <Card elevation="sm" padding="md" className="tk-charter-pillar">
              <div className="tk-charter-pillar__icon tk-charter-pillar__icon--sage">
                <Lock size={20} />
              </div>
              <h4 className="tk-charter-pillar__title">Dignified Anonymity</h4>
              <p className="tk-charter-pillar__desc">
                No degrading photos or public exposure. Every neighbor is served quietly and with respect.
              </p>
            </Card>

            <Card elevation="sm" padding="md" className="tk-charter-pillar">
              <div className="tk-charter-pillar__icon tk-charter-pillar__icon--mint">
                <Users size={20} />
              </div>
              <h4 className="tk-charter-pillar__title">Community Assembly</h4>
              <p className="tk-charter-pillar__desc">
                Relief priorities are decided collaboratively through monthly open neighborhood circles.
              </p>
            </Card>
          </div>
        </div>
      </div>
    </section>
  );
}
