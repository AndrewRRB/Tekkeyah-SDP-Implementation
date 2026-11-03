import { useState } from 'react';
import { RotateCw, Download, ExternalLink, Heart } from 'lucide-react';
import Card from '../common/Card';
import Badge from '../common/Badge';
import Button from '../common/Button';
import EmptyState from '../common/EmptyState';
import { DEFAULT_RECEIPTS_HISTORY } from '../../data/donationPresets';
import './ReceiptsHistoryTable.css';

export default function ReceiptsHistoryTable({ receipts = DEFAULT_RECEIPTS_HISTORY, onViewReceipt, onGoToDonate }) {
  const [filter, setFilter] = useState('ALL'); // 'ALL' | 'MONETARY' | 'IN_KIND'
  const [isRefreshing, setIsRefreshing] = useState(false);

  const filteredReceipts = receipts.filter((r) => {
    if (filter === 'MONETARY') return r.type === 'Monetary';
    if (filter === 'IN_KIND') return r.type === 'Meal' || r.type === 'In-Kind';
    return true;
  });

  const handleRefresh = () => {
    setIsRefreshing(true);
    setTimeout(() => setIsRefreshing(false), 400);
  };

  return (
    <Card elevation="sm" padding="none" className="tk-history-card">
      {/* Table Header & Filters */}
      <div className="tk-history-card__header">
        <div>
          <h3 className="tk-history-card__title">Donation History & Receipts</h3>
          <p className="tk-history-card__subtitle">
            Immutable log of all contributions and instant receipt access.
          </p>
        </div>

        <div className="tk-history-card__controls">
          <div className="tk-history-card__filters" role="tablist">
            <button
              type="button"
              className={`tk-history-card__filter-btn ${filter === 'ALL' ? 'tk-history-card__filter-btn--active' : ''}`}
              onClick={() => setFilter('ALL')}
            >
              All Types
            </button>
            <button
              type="button"
              className={`tk-history-card__filter-btn ${filter === 'MONETARY' ? 'tk-history-card__filter-btn--active' : ''}`}
              onClick={() => setFilter('MONETARY')}
            >
              Monetary
            </button>
            <button
              type="button"
              className={`tk-history-card__filter-btn ${filter === 'IN_KIND' ? 'tk-history-card__filter-btn--active' : ''}`}
              onClick={() => setFilter('IN_KIND')}
            >
              Meals & Goods
            </button>
          </div>

          <button
            type="button"
            className={`tk-history-card__refresh-btn ${isRefreshing ? 'tk-history-card__refresh-btn--spinning' : ''}`}
            onClick={handleRefresh}
            aria-label="Refresh table records"
          >
            <RotateCw size={15} />
          </button>
        </div>
      </div>

      {/* Table / Empty State */}
      {filteredReceipts.length > 0 ? (
        <div className="tk-history-table-container">
          <table className="tk-history-table">
            <thead>
              <tr>
                <th>DATE</th>
                <th>RECEIPT REF</th>
                <th>TYPE</th>
                <th>CAUSE / PROGRAM</th>
                <th>AMOUNT</th>
                <th>PAYMENT METHOD</th>
                <th>STATUS</th>
                <th className="tk-history-table__th--right">ACTIONS</th>
              </tr>
            </thead>
            <tbody>
              {filteredReceipts.map((row) => (
                <tr key={row.id}>
                  <td className="tk-history-table__date">{row.date}</td>
                  <td className="tk-history-table__ref">{row.ref}</td>
                  <td>
                    <Badge variant={row.typeBadgeVariant || 'default'} size="sm">
                      {row.type}
                    </Badge>
                  </td>
                  <td className="tk-history-table__desc">{row.description}</td>
                  <td className="tk-history-table__val">
                    ${row.value?.toLocaleString('en-US', { minimumFractionDigits: 2 })}
                  </td>
                  <td className="tk-history-table__method">{row.paymentMethod}</td>
                  <td>
                    <Badge variant={row.statusVariant || 'success'} size="sm">
                      ● {row.status}
                    </Badge>
                  </td>
                  <td className="tk-history-table__actions">
                    <button
                      type="button"
                      className="tk-history-table__modal-btn"
                      onClick={() => onViewReceipt?.(row)}
                    >
                      View Receipt
                    </button>
                    <button
                      type="button"
                      className="tk-history-table__download-btn"
                      onClick={() => alert(`Downloading PDF for ${row.ref}`)}
                      aria-label="Download receipt PDF"
                    >
                      <Download size={15} />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <div className="tk-history-card__empty-wrap">
          <EmptyState
            title="No donations found"
            description="You have not made any contributions in this category yet."
            action={
              onGoToDonate && (
                <Button
                  variant="primary"
                  size="sm"
                  iconLeft={<Heart size={14} />}
                  onClick={onGoToDonate}
                >
                  Make a Donation
                </Button>
              )
            }
          />
        </div>
      )}

      {/* Table Footer */}
      <div className="tk-history-card__footer">
        <span className="tk-history-card__footer-note">
          Showing {filteredReceipts.length} verified records
        </span>

        <button
          type="button"
          className="tk-history-card__lifetime-link"
          onClick={() => alert('Generating full lifetime statement PDF')}
        >
          <span>Download Lifetime Statement (PDF)</span>
          <ExternalLink size={13} />
        </button>
      </div>
    </Card>
  );
}
