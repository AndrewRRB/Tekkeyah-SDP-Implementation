import { useState } from 'react';
import { DollarSign, Utensils, Package, CreditCard, Banknote, ArrowRight, ShieldCheck, CheckCircle2 } from 'lucide-react';
import Card from '../common/Card';
import Button from '../common/Button';
import { donationService } from '../../services/donationService';
import { toCardPayload, validateCard } from '../../utils/card';
import CardDetailsFields from './CardDetailsFields';
import './ContributionWidget.css';

export default function ContributionWidget({ userId, onReceiptGenerated }) {
  const [pathway, setPathway] = useState('MONETARY'); // 'MONETARY' | 'MEAL' | 'IN_KIND'
  const [frequency, setFrequency] = useState('ONE_TIME'); // 'ONE_TIME' | 'MONTHLY'
  const [selectedAmount, setSelectedAmount] = useState(100);
  const [customAmount, setCustomAmount] = useState('');
  const [isCustom, setIsCustom] = useState(false);
  const [beneficiary, setBeneficiary] = useState('General Community Relief Fund');
  const [paymentGateway, setPaymentGateway] = useState('CARD'); // 'CARD' | 'CASH'
  const [card, setCard] = useState({ holderName: '', number: '', expiry: '', cvv: '' });
  const [cardErrors, setCardErrors] = useState({});
  const [submitError, setSubmitError] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Meal state
  const [mealCount, setMealCount] = useState(50);
  const [kitchenDepot, setKitchenDepot] = useState('District 4 Kitchen');

  // In-Kind state
  const [inKindItem, setInKindItem] = useState('Thermal Blankets');
  const [inKindQty, setInKindQty] = useState(15);
  const [warehouseLocation, setWarehouseLocation] = useState('Central Warehouse Bay 4');

  const presetAmounts = [25, 50, 100, 250];

  const handleAmountClick = (amt) => {
    setIsCustom(false);
    setSelectedAmount(amt);
  };

  const handleCustomClick = () => {
    setIsCustom(true);
  };

  const handleCustomChange = (e) => {
    const val = e.target.value.replace(/[^0-9.]/g, '');
    setCustomAmount(val);
    setSelectedAmount(parseFloat(val) || 0);
  };

  const handleCardChange = (next) => {
    setCard(next);
    if (Object.keys(cardErrors).length) setCardErrors({});
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitError(null);
    const payingByCard = pathway === 'MONETARY' && paymentGateway === 'CARD';
    if (payingByCard) {
      const errors = validateCard(card);
      setCardErrors(errors);
      if (Object.keys(errors).length) return;
    }
    setIsSubmitting(true);

    try {
      let receipt;
      if (pathway === 'MONETARY') {
        const finalAmt = isCustom ? parseFloat(customAmount) || 0 : selectedAmount;
        receipt = await donationService.submitMonetaryDonation({
          userId,
          amount: finalAmt,
          paymentMethod: paymentGateway, // 'CARD' | 'CASH' -> PaymentStrategy on the server
          card: payingByCard ? toCardPayload(card) : null,
        });
      } else if (pathway === 'MEAL') {
        receipt = await donationService.submitMealDonation({
          userId,
          name: `${mealCount} hot meals - ${kitchenDepot}`,
          mealCost: mealCount * 7.5,
        });
      } else {
        receipt = await donationService.submitInKindDonation({
          userId,
          items: [`${inKindQty} x ${inKindItem} (${warehouseLocation})`],
          estimatedValue: inKindQty * 25,
        });
      }

      if (payingByCard) setCard({ holderName: '', number: '', expiry: '', cvv: '' });
      onReceiptGenerated?.(receipt);
    } catch (err) {
      setSubmitError(err.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Card elevation="sm" padding="xl" className="tk-donate-card">
      <form onSubmit={handleSubmit} className="tk-donate-form">
        {/* Step 1: Donation Type Selector (Factory Pattern) */}
        <div className="tk-donate-section">
          <label className="tk-donate-section__label">
            1. Select What You Wish to Donate
          </label>
          <div className="tk-donate-pathway-cards">
            {/* Monetary Option */}
            <div
              className={`tk-donate-pathway-card ${pathway === 'MONETARY' ? 'tk-donate-pathway-card--active' : ''}`}
              onClick={() => setPathway('MONETARY')}
            >
              <div className="tk-donate-pathway-card__icon">
                <DollarSign size={22} />
              </div>
              <div className="tk-donate-pathway-card__text">
                <span className="tk-donate-pathway-card__title">Financial Gift</span>
                <span className="tk-donate-pathway-card__desc">
                  Direct relief funding for community pantries and families.
                </span>
              </div>
            </div>

            {/* Meal Option */}
            <div
              className={`tk-donate-pathway-card ${pathway === 'MEAL' ? 'tk-donate-pathway-card--active' : ''}`}
              onClick={() => setPathway('MEAL')}
            >
              <div className="tk-donate-pathway-card__icon">
                <Utensils size={22} />
              </div>
              <div className="tk-donate-pathway-card__text">
                <span className="tk-donate-pathway-card__title">Hot Meals</span>
                <span className="tk-donate-pathway-card__desc">
                  Sponsor freshly prepared communal kitchen meal boxes.
                </span>
              </div>
            </div>

            {/* In-Kind Option */}
            <div
              className={`tk-donate-pathway-card ${pathway === 'IN_KIND' ? 'tk-donate-pathway-card--active' : ''}`}
              onClick={() => setPathway('IN_KIND')}
            >
              <div className="tk-donate-pathway-card__icon">
                <Package size={22} />
              </div>
              <div className="tk-donate-pathway-card__text">
                <span className="tk-donate-pathway-card__title">Supplies & Goods</span>
                <span className="tk-donate-pathway-card__desc">
                  Donate blankets, heaters, or food staples to depots.
                </span>
              </div>
            </div>
          </div>
        </div>

        {/* Step 2: Specific Inputs Based on Selected Type */}
        {pathway === 'MONETARY' && (
          <div className="tk-donate-section">
            <label className="tk-donate-section__label">
              2. Donation Amount & Details
            </label>

            {/* Frequency Toggle */}
            <div className="tk-donate-freq-toggle">
              <button
                type="button"
                className={`tk-donate-freq-btn ${frequency === 'ONE_TIME' ? 'tk-donate-freq-btn--active' : ''}`}
                onClick={() => setFrequency('ONE_TIME')}
              >
                One-time Gift
              </button>
              <button
                type="button"
                className={`tk-donate-freq-btn ${frequency === 'MONTHLY' ? 'tk-donate-freq-btn--active' : ''}`}
                onClick={() => setFrequency('MONTHLY')}
              >
                Monthly Support
              </button>
            </div>

            {/* Amount Chips */}
            <div className="tk-donate-chips">
              {presetAmounts.map((amt) => (
                <button
                  key={amt}
                  type="button"
                  className={`tk-donate-chip ${!isCustom && selectedAmount === amt ? 'tk-donate-chip--active' : ''}`}
                  onClick={() => handleAmountClick(amt)}
                >
                  ${amt}
                </button>
              ))}
              <button
                type="button"
                className={`tk-donate-chip ${isCustom ? 'tk-donate-chip--active' : ''}`}
                onClick={handleCustomClick}
              >
                Custom
              </button>
            </div>

            {isCustom && (
              <div className="tk-donate-custom-input">
                <span className="tk-donate-custom-sign">$</span>
                <input
                  type="text"
                  placeholder="Enter amount (e.g. 150)"
                  value={customAmount}
                  onChange={handleCustomChange}
                  className="tk-donate-input"
                  autoFocus
                />
              </div>
            )}

            {/* Cause Dropdown */}
            <div className="tk-donate-field">
              <label className="tk-donate-field__label">Target Program or Cause</label>
              <select
                className="tk-donate-select"
                value={beneficiary}
                onChange={(e) => setBeneficiary(e.target.value)}
              >
                <option value="General Community Relief Fund">
                  General Community Relief Fund
                </option>
                <option value="Winter Heating & Blanket Aid">
                  Winter Heating & Blanket Aid
                </option>
                <option value="District 4 Communal Kitchen">
                  District 4 Communal Kitchen
                </option>
                <option value="Emergency Food Pantry">
                  Emergency Food Pantry
                </option>
              </select>
            </div>

            {/* Payment Strategy Selection */}
            <div className="tk-donate-field">
              <label className="tk-donate-field__label">Select Payment Method</label>
              <div className="tk-donate-payments">
                <button
                  type="button"
                  aria-pressed={paymentGateway === 'CARD'}
                  className={`tk-donate-payment-card ${paymentGateway === 'CARD' ? 'tk-donate-payment-card--active' : ''}`}
                  onClick={() => setPaymentGateway('CARD')}
                >
                  <CreditCard size={20} />
                  <div>
                    <span className="tk-donate-payment-card__title">Credit / Debit Card</span>
                    <span className="tk-donate-payment-card__desc">Visa, Mastercard, Amex</span>
                  </div>
                </button>

                <button
                  type="button"
                  aria-pressed={paymentGateway === 'CASH'}
                  className={`tk-donate-payment-card ${paymentGateway === 'CASH' ? 'tk-donate-payment-card--active' : ''}`}
                  onClick={() => setPaymentGateway('CASH')}
                >
                  <Banknote size={20} />
                  <div>
                    <span className="tk-donate-payment-card__title">Cash Payment</span>
                    <span className="tk-donate-payment-card__desc">Pay at branch or courier pickup</span>
                  </div>
                </button>
              </div>
            </div>

            {paymentGateway === 'CARD' && (
              <CardDetailsFields value={card} onChange={handleCardChange} errors={cardErrors} />
            )}
          </div>
        )}

        {/* MEAL DONATION DETAILS */}
        {pathway === 'MEAL' && (
          <div className="tk-donate-section">
            <label className="tk-donate-section__label">
              2. Meal Sponsorship Details
            </label>

            <div className="tk-donate-field">
              <label className="tk-donate-field__label">Number of Hot Meals</label>
              <div className="tk-donate-chips">
                {[25, 50, 100, 250].map((cnt) => (
                  <button
                    key={cnt}
                    type="button"
                    className={`tk-donate-chip ${mealCount === cnt ? 'tk-donate-chip--active' : ''}`}
                    onClick={() => setMealCount(cnt)}
                  >
                    {cnt} Meals
                  </button>
                ))}
              </div>
            </div>

            <div className="tk-donate-field">
              <label className="tk-donate-field__label">Kitchen Location</label>
              <select
                className="tk-donate-select"
                value={kitchenDepot}
                onChange={(e) => setKitchenDepot(e.target.value)}
              >
                <option value="District 4 Kitchen">District 4 Kitchen</option>
                <option value="Old City Central Kitchen">Old City Central Kitchen</option>
                <option value="North Gate Regional Depot">North Gate Regional Depot</option>
              </select>
            </div>
          </div>
        )}

        {/* IN-KIND SUPPLIES DETAILS */}
        {pathway === 'IN_KIND' && (
          <div className="tk-donate-section">
            <label className="tk-donate-section__label">
              2. Supplies & Drop-off Details
            </label>

            <div className="tk-donate-field">
              <label className="tk-donate-field__label">Item Category</label>
              <select
                className="tk-donate-select"
                value={inKindItem}
                onChange={(e) => setInKindItem(e.target.value)}
              >
                <option value="Thermal Blankets">Thermal Blankets</option>
                <option value="Ceramic Space Heaters">Ceramic Space Heaters</option>
                <option value="Medical First Aid Kits">Medical First Aid Kits</option>
                <option value="Flour & Rice Sacks">Flour & Rice Sacks</option>
              </select>
            </div>

            <div className="tk-donate-field">
              <label className="tk-donate-field__label">Quantity</label>
              <div className="tk-donate-chips">
                {[5, 15, 30, 50].map((qty) => (
                  <button
                    key={qty}
                    type="button"
                    className={`tk-donate-chip ${inKindQty === qty ? 'tk-donate-chip--active' : ''}`}
                    onClick={() => setInKindQty(qty)}
                  >
                    {qty} Units
                  </button>
                ))}
              </div>
            </div>

            <div className="tk-donate-field">
              <label className="tk-donate-field__label">Warehouse Drop-off Location</label>
              <select
                className="tk-donate-select"
                value={warehouseLocation}
                onChange={(e) => setWarehouseLocation(e.target.value)}
              >
                <option value="Central Warehouse Bay 4">Central Warehouse Bay 4</option>
                <option value="North Gate Cold District Depot">North Gate Cold District Depot</option>
              </select>
            </div>
          </div>
        )}

        {/* Action Button & Badges */}
        <div className="tk-donate-submit-wrap">
          {submitError && (
            <p className="tk-donate-error" role="alert">
              {submitError}
            </p>
          )}
          <Button
            type="submit"
            variant="primary"
            size="lg"
            fullWidth
            isLoading={isSubmitting}
            iconRight={<ArrowRight size={18} />}
          >
            {pathway === 'MONETARY'
              ? `Confirm $${isCustom ? customAmount || 0 : selectedAmount} Donation`
              : pathway === 'MEAL'
              ? `Sponsor ${mealCount} Meals`
              : `Donate ${inKindQty} ${inKindItem}`}
          </Button>

          <div className="tk-donate-guarantees">
            <div className="tk-donate-guarantee">
              <ShieldCheck size={16} />
              <span>Instant Digital Receipt</span>
            </div>
            <div className="tk-donate-guarantee">
              <CheckCircle2 size={16} />
              <span>100% Direct Community Aid</span>
            </div>
          </div>
        </div>
      </form>
    </Card>
  );
}
