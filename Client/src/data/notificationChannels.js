import { Mail, MessageCircle, Megaphone, Smartphone } from 'lucide-react';

/** Mirrors the server's NotificationChannel enum (one NotificationStrategy each). */
export const CHANNEL_OPTIONS = [
  {
    value: 'EMAIL',
    title: 'Email',
    icon: Mail,
    description: 'Receipts, weekly digests and tax documents in your inbox.',
  },
  {
    value: 'SMS',
    title: 'SMS',
    icon: Smartphone,
    description: 'Text messages for urgent reminders and confirmations.',
  },
  {
    value: 'WHATSAPP',
    title: 'WhatsApp',
    icon: MessageCircle,
    description: 'Instant alerts for donations, shifts and receipts.',
  },
  {
    value: 'SOCIAL_MEDIA',
    title: 'Social Media',
    icon: Megaphone,
    description: 'Updates delivered through your linked social account.',
  },
];

export const channelLabel = (value) =>
  CHANNEL_OPTIONS.find((option) => option.value === value)?.title ?? 'Email';
