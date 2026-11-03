import { useCallback, useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
  AlertTriangle,
  BadgeCheck,
  ChevronRight,
  HandHeart,
  HeartHandshake,
  KeyRound,
  Lock,
  Mail,
  Save,
  ShieldCheck,
  SlidersHorizontal,
  Trash2,
  User,
  UserCog,
} from 'lucide-react';
import Navbar from '../components/layout/Navbar';
import Footer from '../components/layout/Footer';
import Button from '../components/common/Button';
import Modal from '../components/common/Modal';
import PhoneField from '../components/forms/PhoneField';
import ChannelSelector from '../components/forms/ChannelSelector';
import { channelLabel } from '../data/notificationChannels';
import { accountService } from '../services/accountService';
import { splitE164, validatePhone } from '../utils/phone';
import './AccountSettingsPage.css';

const MIN_PASSWORD_LENGTH = 8;

const initialsOf = (name = '') =>
  name
    .split(/\s+/)
    .filter(Boolean)
    .slice(0, 2)
    .map((part) => part[0].toUpperCase())
    .join('');

const profileFormFrom = (user) => ({
  fullName: user.name ?? '',
  email: user.email ?? '',
  phone: splitE164(user.phone),
});

/** Small inline status line: { type: 'success' | 'error', text } */
function StatusMessage({ status }) {
  if (!status) return null;
  return (
    <p className={`tk-settings-status tk-settings-status--${status.type}`} role="status">
      {status.text}
    </p>
  );
}

export default function AccountSettingsPage({ user, onUserUpdated, onSignedOut }) {
  const navigate = useNavigate();

  // ---------------- profile ----------------
  const [profile, setProfile] = useState(() => profileFormFrom(user));
  const [profileErrors, setProfileErrors] = useState({});
  const [profileStatus, setProfileStatus] = useState(null);
  const [isSavingProfile, setIsSavingProfile] = useState(false);

  // ---------------- channel ----------------
  const [channel, setChannel] = useState(user.channel);
  const [channelStatus, setChannelStatus] = useState(null);
  const [isSavingChannel, setIsSavingChannel] = useState(false);

  // ---------------- password ----------------
  const [isPasswordOpen, setIsPasswordOpen] = useState(false);
  const [passwords, setPasswords] = useState({ current: '', next: '', confirm: '' });
  const [passwordStatus, setPasswordStatus] = useState(null);
  const [isSavingPassword, setIsSavingPassword] = useState(false);

  // ---------------- delete ----------------
  const [isDeleteOpen, setIsDeleteOpen] = useState(false);
  const [deletePassword, setDeletePassword] = useState('');
  const [deleteError, setDeleteError] = useState(null);
  const [isDeleting, setIsDeleting] = useState(false);

  const applyUser = useCallback(
    (fresh) => {
      onUserUpdated(fresh);
      setProfile(profileFormFrom(fresh));
      setChannel(fresh.channel);
    },
    [onUserUpdated],
  );

  // Refresh from the server once (another device may have changed something)
  useEffect(() => {
    accountService.getAccount(user.id).then(applyUser).catch(() => {});
  }, [user.id, applyUser]);

  const isProfileDirty =
    profile.fullName.trim() !== user.name ||
    profile.email.trim().toLowerCase() !== user.email ||
    profile.phone.dialCode !== splitE164(user.phone).dialCode ||
    profile.phone.number.replace(/\D/g, '').replace(/^0/, '') !== splitE164(user.phone).number;

  const handleProfileSubmit = async (e) => {
    e.preventDefault();
    setProfileStatus(null);
    const errors = {};
    if (!profile.fullName.trim()) errors.fullName = 'Enter your full name.';
    const phoneError = validatePhone(profile.phone);
    if (phoneError) errors.phone = phoneError;
    setProfileErrors(errors);
    if (Object.keys(errors).length) return;

    setIsSavingProfile(true);
    try {
      const fresh = await accountService.updateProfile(user.id, {
        fullName: profile.fullName.trim(),
        email: profile.email.trim(),
        countryCode: profile.phone.dialCode,
        phone: profile.phone.number,
      });
      applyUser(fresh);
      setProfileStatus({ type: 'success', text: 'Profile saved.' });
    } catch (err) {
      setProfileStatus({ type: 'error', text: err.message });
    } finally {
      setIsSavingProfile(false);
    }
  };

  const handleChannelChange = async (next) => {
    if (next === user.channel) return;
    const previous = channel;
    setChannel(next); // optimistic
    setChannelStatus(null);
    setIsSavingChannel(true);
    try {
      const fresh = await accountService.changeChannel(user.id, next);
      applyUser(fresh);
      setChannelStatus({ type: 'success', text: `Notifications will now arrive via ${channelLabel(next)}.` });
    } catch (err) {
      setChannel(previous);
      setChannelStatus({ type: 'error', text: err.message });
    } finally {
      setIsSavingChannel(false);
    }
  };

  const handlePasswordSubmit = async (e) => {
    e.preventDefault();
    setPasswordStatus(null);
    if (passwords.next.length < MIN_PASSWORD_LENGTH) {
      setPasswordStatus({ type: 'error', text: `New password needs at least ${MIN_PASSWORD_LENGTH} characters.` });
      return;
    }
    if (passwords.next !== passwords.confirm) {
      setPasswordStatus({ type: 'error', text: 'The new passwords do not match.' });
      return;
    }
    setIsSavingPassword(true);
    try {
      await accountService.changePassword(user.id, {
        currentPassword: passwords.current,
        newPassword: passwords.next,
      });
      setPasswords({ current: '', next: '', confirm: '' });
      setIsPasswordOpen(false);
      setPasswordStatus({ type: 'success', text: 'Password changed.' });
    } catch (err) {
      setPasswordStatus({ type: 'error', text: err.message });
    } finally {
      setIsSavingPassword(false);
    }
  };

  const closeDeleteModal = () => {
    setIsDeleteOpen(false);
    setDeletePassword('');
    setDeleteError(null);
  };

  const handleDelete = async (e) => {
    e.preventDefault();
    setDeleteError(null);
    setIsDeleting(true);
    try {
      await accountService.deleteAccount(user.id, deletePassword);
      navigate('/', { replace: true });
      onSignedOut();
    } catch (err) {
      setDeleteError(err.message);
      setIsDeleting(false);
    }
  };

  const roles = [
    {
      key: 'donor',
      icon: HandHeart,
      title: 'Donor Profile',
      detail: user.donor
        ? `Lifetime giving: $${user.totalDonated.toLocaleString('en-US', { minimumFractionDigits: 2 })}`
        : 'Created automatically with your first donation',
      linked: user.donor,
    },
    {
      key: 'volunteer',
      icon: HeartHandshake,
      title: 'Volunteer Hub',
      detail: 'Shifts and field tasks',
      linked: user.volunteer,
    },
    {
      key: 'beneficiary',
      icon: ShieldCheck,
      title: 'Beneficiary Space',
      detail: 'Aid programs and distributions',
      linked: user.beneficiary,
    },
  ];

  return (
    <div className="tk-settings-page">
      <Navbar user={user} />

      <main className="tk-settings-main">
        <div className="tk-settings-container">
          <nav className="tk-settings-breadcrumb" aria-label="Breadcrumb">
            <Link to="/">Home</Link>
            <ChevronRight size={14} aria-hidden="true" />
            <span>Account</span>
            <ChevronRight size={14} aria-hidden="true" />
            <span aria-current="page">Control Panel</span>
          </nav>

          <header className="tk-settings-header">
            <h1 className="tk-settings-title">User Control Panel &amp; Preferences</h1>
            <p className="tk-settings-subtitle">
              Manage your Tekkeyah identity, how we reach you, and your account security.
            </p>
          </header>

          <div className="tk-settings-grid">
            {/* ===================== LEFT COLUMN ===================== */}
            <div className="tk-settings-column">
              {/* Profile */}
              <section className="tk-settings-card" aria-labelledby="profile-heading">
                <div className="tk-settings-card__head">
                  <div>
                    <p className="tk-settings-eyebrow">FR-USR-04</p>
                    <h2 id="profile-heading" className="tk-settings-card__title">
                      Profile Management
                    </h2>
                  </div>
                  <span className="tk-settings-pill">
                    <span className="tk-settings-pill__dot" aria-hidden="true" />
                    Active Member
                  </span>
                </div>

                <div className="tk-settings-identity">
                  <div className="tk-settings-avatar" aria-hidden="true">
                    {initialsOf(user.name) || <User size={26} />}
                  </div>
                  <div className="tk-settings-identity__text">
                    <span className="tk-settings-identity__name">{user.name}</span>
                    <span className="tk-settings-identity__meta">
                      {user.role} · Member #{user.id}
                    </span>
                  </div>
                </div>

                <form className="tk-settings-form" onSubmit={handleProfileSubmit} noValidate>
                  <div className="tk-settings-field">
                    <label htmlFor="settings-name" className="tk-settings-label">
                      Full Name
                    </label>
                    <div className="tk-settings-input-wrap">
                      <User size={16} className="tk-settings-input-icon" aria-hidden="true" />
                      <input
                        id="settings-name"
                        className="tk-settings-input"
                        autoComplete="name"
                        value={profile.fullName}
                        onChange={(e) => setProfile((p) => ({ ...p, fullName: e.target.value }))}
                        aria-invalid={Boolean(profileErrors.fullName)}
                      />
                    </div>
                    {profileErrors.fullName && <p className="tk-settings-field-error">{profileErrors.fullName}</p>}
                  </div>

                  <div className="tk-settings-field">
                    <label htmlFor="settings-email" className="tk-settings-label">
                      Email Address
                    </label>
                    <div className="tk-settings-input-wrap">
                      <Mail size={16} className="tk-settings-input-icon" aria-hidden="true" />
                      <input
                        id="settings-email"
                        type="email"
                        className="tk-settings-input"
                        autoComplete="email"
                        value={profile.email}
                        onChange={(e) => setProfile((p) => ({ ...p, email: e.target.value }))}
                      />
                    </div>
                  </div>

                  <PhoneField
                    value={profile.phone}
                    onChange={(phone) => {
                      setProfile((p) => ({ ...p, phone }));
                      setProfileErrors((prev) => ({ ...prev, phone: undefined }));
                    }}
                    error={profileErrors.phone}
                  />

                  <div className="tk-settings-actions">
                    <StatusMessage status={profileStatus} />
                    <Button
                      type="submit"
                      variant="primary"
                      iconLeft={<Save size={16} />}
                      isLoading={isSavingProfile}
                      disabled={!isProfileDirty}
                    >
                      Save Profile Changes
                    </Button>
                  </div>
                </form>
              </section>

              {/* Security + delete */}
              <section className="tk-settings-card" aria-labelledby="security-heading">
                <div className="tk-settings-card__head tk-settings-card__head--icon">
                  <span className="tk-settings-card__icon" aria-hidden="true">
                    <Lock size={18} />
                  </span>
                  <div>
                    <h2 id="security-heading" className="tk-settings-card__title">
                      Account Security &amp; Privacy
                    </h2>
                    <p className="tk-settings-card__desc">Password and account lifecycle.</p>
                  </div>
                </div>

                <div className="tk-settings-panel">
                  <div className="tk-settings-panel__row">
                    <div>
                      <p className="tk-settings-panel__title">Password</p>
                      <p className="tk-settings-panel__desc">
                        Use at least {MIN_PASSWORD_LENGTH} characters. You will need your current password.
                      </p>
                    </div>
                    <Button
                      variant="light"
                      size="sm"
                      iconLeft={<KeyRound size={14} />}
                      onClick={() => {
                        setIsPasswordOpen((open) => !open);
                        setPasswordStatus(null);
                      }}
                      aria-expanded={isPasswordOpen}
                    >
                      {isPasswordOpen ? 'Cancel' : 'Change Password'}
                    </Button>
                  </div>

                  {isPasswordOpen && (
                    <form className="tk-settings-password" onSubmit={handlePasswordSubmit}>
                      {[
                        ['current', 'Current password', 'current-password'],
                        ['next', 'New password', 'new-password'],
                        ['confirm', 'Confirm new password', 'new-password'],
                      ].map(([field, label, autoComplete]) => (
                        <div key={field} className="tk-settings-field">
                          <label htmlFor={`pw-${field}`} className="tk-settings-label">
                            {label}
                          </label>
                          <input
                            id={`pw-${field}`}
                            type="password"
                            required
                            autoComplete={autoComplete}
                            className="tk-settings-input tk-settings-input--plain"
                            value={passwords[field]}
                            onChange={(e) => setPasswords((p) => ({ ...p, [field]: e.target.value }))}
                          />
                        </div>
                      ))}
                      <Button type="submit" variant="primary" size="sm" isLoading={isSavingPassword}>
                        Update Password
                      </Button>
                    </form>
                  )}
                  <StatusMessage status={passwordStatus} />
                </div>

                <div className="tk-settings-danger">
                  <div>
                    <p className="tk-settings-danger__title">
                      <AlertTriangle size={14} aria-hidden="true" /> Delete Account
                    </p>
                    <p className="tk-settings-danger__desc">
                      Removes your profile and roles permanently. Your past donations stay on record
                      anonymously for the foundation&apos;s tax and audit records.
                    </p>
                  </div>
                  <button type="button" className="tk-settings-danger__btn" onClick={() => setIsDeleteOpen(true)}>
                    <Trash2 size={15} aria-hidden="true" />
                    Delete Account
                  </button>
                </div>
              </section>
            </div>

            {/* ===================== RIGHT COLUMN ===================== */}
            <div className="tk-settings-column">
              <section className="tk-settings-card" aria-labelledby="channel-heading">
                <div className="tk-settings-card__head">
                  <div>
                    <p className="tk-settings-eyebrow">FR-USR-05 · FR-COM-02</p>
                    <h2 id="channel-heading" className="tk-settings-card__title">
                      Notification Channel
                    </h2>
                    <p className="tk-settings-card__desc">
                      Choose how donation receipts, shift changes and updates reach you.
                    </p>
                  </div>
                  <SlidersHorizontal size={20} className="tk-settings-card__corner-icon" aria-hidden="true" />
                </div>

                <div className="tk-settings-callout">
                  <BadgeCheck size={18} aria-hidden="true" />
                  <p>
                    <strong>Currently active:</strong> {channelLabel(user.channel)}
                    {(user.channel === 'SMS' || user.channel === 'WHATSAPP') && user.phone && (
                      <> to <span className="tk-settings-mono">
                          {splitE164(user.phone).dialCode} {splitE164(user.phone).number}
                        </span></>
                    )}
                    {user.channel === 'EMAIL' && <> to {user.email}</>}
                  </p>
                </div>

                <ChannelSelector
                  name="settings-channel"
                  value={channel}
                  activeValue={user.channel}
                  onChange={handleChannelChange}
                  disabled={isSavingChannel}
                />
                <StatusMessage status={channelStatus} />
              </section>

              <section className="tk-settings-card" aria-labelledby="roles-heading">
                <div className="tk-settings-card__head">
                  <div>
                    <h2 id="roles-heading" className="tk-settings-card__title">
                      Linked Roles
                    </h2>
                    <p className="tk-settings-card__desc">Role profiles attached to your account.</p>
                  </div>
                  <UserCog size={20} className="tk-settings-card__corner-icon" aria-hidden="true" />
                </div>

                <ul className="tk-settings-roles">
                  {roles.map(({ key, icon: Icon, title, detail, linked }) => (
                    <li key={key} className={`tk-settings-role ${linked ? '' : 'tk-settings-role--off'}`}>
                      <span className="tk-settings-role__icon" aria-hidden="true">
                        <Icon size={17} />
                      </span>
                      <span className="tk-settings-role__text">
                        <span className="tk-settings-role__title">{title}</span>
                        <span className="tk-settings-role__detail">{detail}</span>
                      </span>
                      <span className={`tk-settings-role__badge ${linked ? 'tk-settings-role__badge--on' : ''}`}>
                        {linked ? 'Linked' : 'Not linked'}
                      </span>
                    </li>
                  ))}
                </ul>
              </section>
            </div>
          </div>
        </div>
      </main>

      <Modal
        isOpen={isDeleteOpen}
        onClose={closeDeleteModal}
        title="Delete your account?"
        subtitle="This cannot be undone."
        maxWidth="460px"
      >
        <form className="tk-settings-delete" onSubmit={handleDelete}>
          <p className="tk-settings-delete__text">
            Your profile, notification settings and role profiles will be removed. Donations you made stay
            in the foundation&apos;s records without your name.
          </p>
          <div className="tk-settings-field">
            <label htmlFor="delete-password" className="tk-settings-label">
              Enter your password to confirm
            </label>
            <input
              id="delete-password"
              type="password"
              required
              autoComplete="current-password"
              autoFocus
              className="tk-settings-input tk-settings-input--plain"
              value={deletePassword}
              onChange={(e) => setDeletePassword(e.target.value)}
            />
          </div>
          {deleteError && <StatusMessage status={{ type: 'error', text: deleteError }} />}
          <div className="tk-settings-delete__actions">
            <Button variant="light" onClick={closeDeleteModal} disabled={isDeleting}>
              Keep Account
            </Button>
            <button
              type="submit"
              className="tk-settings-danger__btn tk-settings-danger__btn--solid"
              disabled={!deletePassword || isDeleting}
            >
              <Trash2 size={15} aria-hidden="true" />
              {isDeleting ? 'Deleting…' : 'Delete Permanently'}
            </button>
          </div>
        </form>
      </Modal>

      <Footer />
    </div>
  );
}
