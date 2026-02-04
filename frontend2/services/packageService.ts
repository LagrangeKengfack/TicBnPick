export type PackageCreationPayload = {
  senderId: string;
  recipientName: string;
  weightKg?: number;
  description?: string;
};

export const packageService = {
  createPackage: async (payload: PackageCreationPayload) => {
    // stub: simulate creation and return a tracking number to satisfy frontend
    const tracking = 'TRK' + Date.now();
    return Promise.resolve({ id: 'pkg-' + Date.now(), trackingNumber: tracking, tracking_number: tracking, ...payload });
  }
};

export default packageService;
