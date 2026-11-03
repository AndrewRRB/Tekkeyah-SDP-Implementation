import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Heart, Users, LifeBuoy, ArrowRight, Truck, ChefHat, Package, Check } from 'lucide-react';
import Card from '../common/Card';
import Badge from '../common/Badge';
import Button from '../common/Button';
import './ActionCards.css';

export default function ActionCards() {
  const navigate = useNavigate();
  const [selectedAmount, setSelectedAmount] = useState(50);

  const amounts = [25, 50, 100, 250];

  return (
    <section className="tk-action-cards-section">
      <div className="tk-action-cards-grid">
        {/* Card 1: Make a Contribution */}
        <Card elevation="sm" padding="lg" hover className="tk-action-card">
          <div className="tk-action-card__header">
            <div className="tk-action-card__icon-box">
              <Heart size={20} />
            </div>
            <Badge variant="default" size="sm">
              100% AUDITED
            </Badge>
          </div>

          <h3 className="tk-action-card__title">Make a Contribution</h3>
          <p className="tk-action-card__desc">
            Fund nutritious communal pantries, cold-weather heating reserves, and vital prescription assistance directly.
          </p>

          <div className="tk-action-card__amount-selector">
            <span className="tk-action-card__sublabel">SELECT GIFT AMOUNT</span>
            <div className="tk-action-card__chips">
              {amounts.map((amt) => (
                <button
                  key={amt}
                  type="button"
                  className={`tk-action-card__chip ${selectedAmount === amt ? 'tk-action-card__chip--active' : ''}`}
                  onClick={() => setSelectedAmount(amt)}
                >
                  ${amt}
                </button>
              ))}
            </div>
          </div>

          <Button
            variant="primary"
            fullWidth
            size="md"
            iconRight={<ArrowRight size={16} />}
            onClick={() => navigate('/giving-hub', { state: { amount: selectedAmount } })}
          >
            Donate Now
          </Button>
        </Card>

        {/* Card 2: Lend a Hand */}
        <Card elevation="sm" padding="lg" hover className="tk-action-card">
          <div className="tk-action-card__header">
            <div className="tk-action-card__icon-box">
              <Users size={20} />
            </div>
            <Badge variant="default" size="sm">
              16 SHIFTS OPEN
            </Badge>
          </div>

          <h3 className="tk-action-card__title">Lend a Hand</h3>
          <p className="tk-action-card__desc">
            Join our decentralized volunteer corps. Offer your culinary talents, vehicle transport, or warehouse coordination.
          </p>

          <div className="tk-action-card__shifts-list">
            <div className="tk-action-card__shift-item">
              <div className="tk-action-card__shift-role">
                <Truck size={15} />
                <span>Volunteer Meal Route Drivers</span>
              </div>
              <span className="tk-action-card__shift-count">4 open</span>
            </div>

            <div className="tk-action-card__shift-item">
              <div className="tk-action-card__shift-role">
                <ChefHat size={15} />
                <span>Commercial Kitchen Prep Crew</span>
              </div>
              <span className="tk-action-card__shift-count">8 open</span>
            </div>

            <div className="tk-action-card__shift-item">
              <div className="tk-action-card__shift-role">
                <Package size={15} />
                <span>Weekend Pantry Distribution</span>
              </div>
              <span className="tk-action-card__shift-count">3 open</span>
            </div>
          </div>

          <Button
            variant="secondary"
            fullWidth
            size="md"
            iconRight={<ArrowRight size={16} />}
            onClick={() => navigate('/auth')}
          >
            Join Volunteer Network
          </Button>
        </Card>

        {/* Card 3: Community Support Desk */}
        <Card elevation="sm" padding="lg" hover className="tk-action-card">
          <div className="tk-action-card__header">
            <div className="tk-action-card__icon-box">
              <LifeBuoy size={20} />
            </div>
            <Badge variant="default" size="sm">
              ZERO STIGMA
            </Badge>
          </div>

          <h3 className="tk-action-card__title">Community Support Desk</h3>
          <p className="tk-action-card__desc">
            Every human deserves sanctuary and warmth. Confidential support with food allowances, thermal supplies, and social advocacy.
          </p>

          <div className="tk-action-card__bullets">
            <div className="tk-action-card__bullet">
              <Check size={16} className="tk-action-card__bullet-icon" />
              <span>Anonymous grocery codes sent via encrypted SMS directly.</span>
            </div>
            <div className="tk-action-card__bullet">
              <Check size={16} className="tk-action-card__bullet-icon" />
              <span>Emergency utility & heating oil vouchers granted within 4 hrs.</span>
            </div>
          </div>

          <Button
            variant="secondary"
            fullWidth
            size="md"
            iconRight={<ArrowRight size={16} />}
            onClick={() => navigate('/auth')}
          >
            Learn About Family Aid
          </Button>
        </Card>
      </div>
    </section>
  );
}
