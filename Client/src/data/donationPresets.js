/**
 * Decoupled data contracts and default records for the Giving Hub.
 * Keeps UI components clean and clutter-free per requirements.
 */
export const DEFAULT_RECEIPTS_HISTORY = [
  {
    id: 'rec-01',
    date: 'Oct 24, 2024',
    ref: '#TK-2025-0891',
    type: 'Monetary',
    typeBadgeVariant: 'default',
    description: 'District 4 Winter Relief Fund',
    value: 250.00,
    paymentMethod: 'Card •••• 4821',
    status: 'COMPLETED',
    statusVariant: 'success',
  },
  {
    id: 'rec-02',
    date: 'Oct 18, 2024',
    ref: '#TK-2025-0744',
    type: 'Meal',
    typeBadgeVariant: 'info',
    description: '50 Hot Lamb & Rice Baskets',
    value: 350.00,
    paymentMethod: 'Kitchen Drop-off',
    status: 'COMPLETED',
    statusVariant: 'success',
  },
  {
    id: 'rec-03',
    date: 'Oct 02, 2024',
    ref: '#TK-2025-0610',
    type: 'In-Kind',
    typeBadgeVariant: 'warning',
    description: '15 Thermal Blankets & Space Heaters',
    value: 450.00,
    paymentMethod: 'Warehouse Bay 4',
    status: 'COMPLETED',
    statusVariant: 'success',
  },
  {
    id: 'rec-04',
    date: 'Sep 14, 2024',
    ref: '#TK-2025-0488',
    type: 'Monetary',
    typeBadgeVariant: 'default',
    description: 'Emergency Heating Initiative',
    value: 100.00,
    paymentMethod: 'Cash Payment',
    status: 'COMPLETED',
    statusVariant: 'success',
  },
  {
    id: 'rec-05',
    date: 'Aug 29, 2024',
    ref: '#TK-2025-0219',
    type: 'Monetary',
    typeBadgeVariant: 'default',
    description: 'Pantry Maintenance Fund',
    value: 50.00,
    paymentMethod: 'Card •••• 4821',
    status: 'REFUNDED',
    statusVariant: 'critical',
  },
];

export const DEFAULT_DONATION_METRICS = {
  lifetimeDonated: 4850.00,
  growthPct: 12,
  confirmedSeedsCount: 18,
  totalMealsFunded: 412,
  kitchenDepotLog: 'District 4 Kitchen log',
};
