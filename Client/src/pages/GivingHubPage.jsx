import { useState } from 'react';
import Navbar from '../components/layout/Navbar';
import GivingHeader from '../components/giving/GivingHeader';
import ContributionWidget from '../components/giving/ContributionWidget';
import ReceiptsHistoryTable from '../components/giving/ReceiptsHistoryTable';
import ReceiptModal from '../components/giving/ReceiptModal';
import Footer from '../components/layout/Footer';
import { DEFAULT_RECEIPTS_HISTORY } from '../data/donationPresets';
import './GivingHubPage.css';

export default function GivingHubPage({ user }) {
  const [activeTab, setActiveTab] = useState('DONATE'); // 'DONATE' | 'HISTORY'
  const [receiptsList, setReceiptsList] = useState(DEFAULT_RECEIPTS_HISTORY);
  const [activeReceipt, setActiveReceipt] = useState(null);
  const [isReceiptModalOpen, setIsReceiptModalOpen] = useState(false);

  // Handle new donation submission
  const handleReceiptGenerated = (newReceipt) => {
    const formatted = {
      id: `rec-${Date.now()}`,
      date: new Date().toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' }),
      ref: newReceipt.receiptNumber || `#TK-2025-${Math.floor(1000 + Math.random() * 9000)}`,
      type: newReceipt.donationType || 'Monetary',
      typeBadgeVariant: newReceipt.donationType === 'Meal' ? 'info' : newReceipt.donationType === 'In-Kind' ? 'warning' : 'default',
      description: newReceipt.beneficiary || newReceipt.itemDescription || 'Direct Community Aid',
      value: newReceipt.amount || newReceipt.value || 100,
      paymentMethod: newReceipt.paymentMethod || 'Credit / Debit Card',
      status: 'COMPLETED',
      statusVariant: 'success',
    };

    setReceiptsList((prev) => [formatted, ...prev]);
    setActiveReceipt(formatted);
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
              <ContributionWidget onReceiptGenerated={handleReceiptGenerated} />
            </div>
          )}

          {/* Tab 2: Full-width donation history */}
          {activeTab === 'HISTORY' && (
            <div className="tk-giving-hub-content tk-giving-hub-content--fade">
              <ReceiptsHistoryTable
                receipts={receiptsList}
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
