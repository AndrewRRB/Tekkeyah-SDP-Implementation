import { useCallback, useEffect, useState } from 'react';
import Navbar from '../components/layout/Navbar';
import GivingHeader from '../components/giving/GivingHeader';
import ContributionWidget from '../components/giving/ContributionWidget';
import ReceiptsHistoryTable from '../components/giving/ReceiptsHistoryTable';
import ReceiptModal from '../components/giving/ReceiptModal';
import Footer from '../components/layout/Footer';
import { donationService } from '../services/donationService';
import './GivingHubPage.css';

const TYPE_BADGE_VARIANT = { Monetary: 'default', Meal: 'info', 'In-Kind': 'warning' };
const PAYMENT_METHOD_LABEL = { CARD: 'Credit / Debit Card', CASH: 'Cash Payment', CHECK: 'Check' };
const DELIVERY_LABEL = { Meal: 'Kitchen Drop-off', 'In-Kind': 'Physical Delivery' };

// Server DonationResponse -> row shape used by the table and the receipt modal.
const toReceiptRow = (donation) => ({
  id: donation.donationId,
  date: donation.issuedAt
    ? new Date(donation.issuedAt).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })
    : '',
  ref: donation.receiptNumber,
  type: donation.type,
  typeBadgeVariant: TYPE_BADGE_VARIANT[donation.type] || 'default',
  description: donation.description,
  value: donation.amount,
  paymentMethod:
    PAYMENT_METHOD_LABEL[donation.paymentMethod] || donation.transactionRef || DELIVERY_LABEL[donation.type] || '',
  status: 'COMPLETED',
  statusVariant: 'success',
});

export default function GivingHubPage({ user }) {
  const [activeTab, setActiveTab] = useState('DONATE'); // 'DONATE' | 'HISTORY'
  const [receiptsList, setReceiptsList] = useState([]);
  const [isLoadingHistory, setIsLoadingHistory] = useState(true);
  const [historyError, setHistoryError] = useState(null);
  const [activeReceipt, setActiveReceipt] = useState(null);
  const [isReceiptModalOpen, setIsReceiptModalOpen] = useState(false);

  // State is only set in the promise callbacks, after the request settles
  const fetchHistory = useCallback(
    () =>
      donationService
        .getDonationHistory(user.id)
        .then((donations) => {
          setReceiptsList(donations.map(toReceiptRow));
          setHistoryError(null);
        })
        .catch((err) => setHistoryError(err.message))
        .finally(() => setIsLoadingHistory(false)),
    [user.id],
  );

  useEffect(() => {
    fetchHistory();
  }, [fetchHistory]);

  const refreshHistory = () => {
    setIsLoadingHistory(true);
    return fetchHistory();
  };

  // Called with the saved donation returned by the server
  const handleReceiptGenerated = (savedDonation) => {
    const row = toReceiptRow(savedDonation);
    setReceiptsList((prev) => [row, ...prev]);
    setActiveReceipt(row);
    setIsReceiptModalOpen(true);
  };

  const handleViewReceipt = (receipt) => {
    setActiveReceipt(receipt);
    setIsReceiptModalOpen(true);
  };

  return (
    <div className="tk-giving-hub-page">
      <Navbar user={user} />

      <main className="tk-giving-hub-main">
        <div className="tk-giving-hub-container">
          {/* Header with Make a Donation / History Tabs */}
          <GivingHeader
            activeTab={activeTab}
            onTabChange={setActiveTab}
            historyCount={receiptsList.length}
          />

          {/* Tab 1: Full-width clean donation form */}
          {activeTab === 'DONATE' && (
            <div className="tk-giving-hub-content tk-giving-hub-content--fade">
              <ContributionWidget userId={user.id} onReceiptGenerated={handleReceiptGenerated} />
            </div>
          )}

          {/* Tab 2: Full-width donation history */}
          {activeTab === 'HISTORY' && (
            <div className="tk-giving-hub-content tk-giving-hub-content--fade">
              {historyError && (
                <p className="tk-giving-hub-error" role="alert">
                  Could not load your donations: {historyError}
                </p>
              )}
              <ReceiptsHistoryTable
                receipts={receiptsList}
                isLoading={isLoadingHistory}
                onRefresh={refreshHistory}
                onViewReceipt={handleViewReceipt}
                onGoToDonate={() => setActiveTab('DONATE')}
              />
            </div>
          )}
        </div>
      </main>

      {/* Digital Receipt Modal */}
      <ReceiptModal
        isOpen={isReceiptModalOpen}
        onClose={() => setIsReceiptModalOpen(false)}
        receipt={activeReceipt}
      />

      <Footer />
    </div>
  );
}
