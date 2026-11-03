import { Download, CheckCircle } from 'lucide-react';
import Modal from '../common/Modal';
import Badge from '../common/Badge';
import Button from '../common/Button';
import './ReceiptModal.css';

export default function ReceiptModal({ isOpen, onClose, receipt }) {
  if (!receipt) return null;

  const displayDate = receipt.date || (receipt.timestamp ? new Date(receipt.timestamp).toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
    year: 'numeric',
  }) : 'Today');

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title="Contribution Receipt"
      subtitle="Official verified receipt"
      maxWidth="480px"
    >
      <div className="tk-receipt-modal">
        {/* Verification Banner */}
        <div className="tk-receipt-modal__status-banner">
          <div className="tk-receipt-modal__icon">
            <CheckCircle size={22} />
          </div>
          <div className="tk-receipt-modal__status-text">
            <span className="tk-receipt-modal__status-title">Contribution Confirmed</span>
            <span className="tk-receipt-modal__status-sub">
              Tekkeyah Community Foundation
            </span>
          </div>
        </div>

        {/* Details Card */}
        <div className="tk-receipt-modal__table">
          <div className="tk-receipt-modal__row">
            <span className="tk-receipt-modal__label">Receipt Reference</span>
            <span className="tk-receipt-modal__value tk-receipt-modal__value--mono">
              {receipt.ref || receipt.receiptNumber}
            </span>
          </div>

          <div className="tk-receipt-modal__row">
            <span className="tk-receipt-modal__label">Date</span>
            <span className="tk-receipt-modal__value">{displayDate}</span>
          </div>

          <div className="tk-receipt-modal__row">
            <span className="tk-receipt-modal__label">Type</span>
            <Badge variant="default" size="sm">
              {receipt.type || receipt.donationType || 'Monetary'}
            </Badge>
          </div>

          <div className="tk-receipt-modal__row">
            <span className="tk-receipt-modal__label">Cause</span>
            <span className="tk-receipt-modal__value">
              {receipt.description || receipt.beneficiary || 'General Community Relief'}
            </span>
          </div>

          <div className="tk-receipt-modal__row">
            <span className="tk-receipt-modal__label">Payment Method</span>
            <span className="tk-receipt-modal__value">{receipt.paymentMethod}</span>
          </div>

          <div className="tk-receipt-modal__row tk-receipt-modal__row--highlight">
            <span className="tk-receipt-modal__label">Declared Value</span>
            <span className="tk-receipt-modal__val-highlight">
              ${(receipt.value || receipt.amount || 0).toLocaleString('en-US', { minimumFractionDigits: 2 })}
            </span>
          </div>
        </div>

        {/* Actions */}
        <div className="tk-receipt-modal__actions">
          <Button
            variant="light"
            size="md"
            iconLeft={<Download size={16} />}
            onClick={() => alert(`Downloading official PDF for receipt ${receipt.ref || receipt.receiptNumber}`)}
          >
            Download PDF
          </Button>

          <Button
            variant="primary"
            size="md"
            onClick={onClose}
          >
            Done
          </Button>
        </div>
      </div>
    </Modal>
  );
}
