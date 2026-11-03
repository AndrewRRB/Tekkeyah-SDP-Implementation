import { useNavigate } from 'react-router-dom';
import { Heart, Users } from 'lucide-react';
import Button from '../common/Button';
import './CtaBanner.css';

export default function CtaBanner() {
  const navigate = useNavigate();

  return (
    <section className="tk-cta-section" id="volunteer">
      <div className="tk-cta-container">
        <div className="tk-cta-box">
          <h2 className="tk-cta-title">Ready to Walk Alongside Your Neighbors?</h2>
          <p className="tk-cta-desc">
            Whether you have an hour to spare this weekend, a bag of dry legumes,
            or financial means to sponsor a family pantry—your presence matters.
          </p>

          <div className="tk-cta-actions">
            <Button
              variant="light"
              size="lg"
              iconLeft={<Heart size={18} />}
              onClick={() => navigate('/giving-hub')}
            >
              Make a Contribution
            </Button>

            <Button
              variant="outline"
              size="lg"
              className="tk-cta-outline-btn"
              iconLeft={<Users size={18} />}
              onClick={() => navigate('/auth')}
            >
              Register as a Volunteer
            </Button>
          </div>
        </div>
      </div>
    </section>
  );
}
