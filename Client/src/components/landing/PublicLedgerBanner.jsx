import './PublicLedgerBanner.css';

export default function PublicLedgerBanner({ stats }) {
  const currentStats = stats || {
    totalFunds: '$342,850',
    hotMeals: '18,420',
    activeVolunteers: '312',
    reconciledPct: '100%',
  };

  return (
    <section className="tk-ledger-bar-container">
      <div className="tk-ledger-bar">
        <div className="tk-ledger-bar__badge-label">
          <span className="tk-ledger-bar__dot" />
          <span className="tk-ledger-bar__title">LIVE AUDITED PUBLIC LEDGER</span>
          <span className="tk-ledger-bar__desc">
            Reconciled hourly with zero administrative overhead take:
          </span>
        </div>

        <div className="tk-ledger-bar__metrics">
          <div className="tk-ledger-bar__metric">
            <span className="tk-ledger-bar__num">{currentStats.totalFunds}</span>
            <span className="tk-ledger-bar__caption">TOTAL FUNDS RAISED</span>
          </div>

          <div className="tk-ledger-bar__metric">
            <span className="tk-ledger-bar__num">{currentStats.hotMeals}</span>
            <span className="tk-ledger-bar__caption">HOT MEALS DISTRIBUTED</span>
          </div>

          <div className="tk-ledger-bar__metric">
            <span className="tk-ledger-bar__num">{currentStats.activeVolunteers}</span>
            <span className="tk-ledger-bar__caption">ACTIVE VOLUNTEERS ON DUTY</span>
          </div>

          <div className="tk-ledger-bar__metric">
            <span className="tk-ledger-bar__num">{currentStats.reconciledPct}</span>
            <span className="tk-ledger-bar__caption">ON-CHAIN RECONCILED</span>
          </div>
        </div>
      </div>
    </section>
  );
}
