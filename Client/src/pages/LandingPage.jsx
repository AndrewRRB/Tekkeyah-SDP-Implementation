import Navbar from '../components/layout/Navbar';
import LandingHero from '../components/landing/LandingHero';
import ActionCards from '../components/landing/ActionCards';
import PublicLedgerBanner from '../components/landing/PublicLedgerBanner';
import CampaignsSection from '../components/landing/CampaignsSection';
import CommunityCharter from '../components/landing/CommunityCharter';
import CtaBanner from '../components/landing/CtaBanner';
import Footer from '../components/layout/Footer';
import './LandingPage.css';

export default function LandingPage({ user }) {
  return (
    <div className="tk-landing-page">
      <Navbar user={user} />
      <main>
        <LandingHero />
        <ActionCards />
        <PublicLedgerBanner />
        <CampaignsSection />
        <CommunityCharter />
        <CtaBanner />
      </main>
      <Footer />
    </div>
  );
}
