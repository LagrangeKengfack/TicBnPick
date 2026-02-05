export interface PackageCreationPayload {
  [key: string]: any;
}

function generateMockTrackingNumber(prefix = "TII", digits = 6) {
  const rand = Math.floor(Math.random() * Math.pow(10, digits))
    .toString()
    .padStart(digits, "0");
  const ts = Date.now().toString().slice(-4);
  return `${prefix}-${ts}-${rand}`;
}

export const packageService = {
  /**
   * Simulate creating a package on the backend.
   * Returns:
   *  { success: boolean, id: string, trackingNumber: string, tracking_number: string }
   */
  createPackage: async (data: PackageCreationPayload) => {
    console.info("Mock createPackage called", data);

    // Simulate small latency to reproduce a realistic dev UX
    await new Promise((resolve) => setTimeout(resolve, 400));

    const trackingNumber = generateMockTrackingNumber();

    return {
      success: true,
      id: `mock-${Math.floor(Math.random() * 100000)}`,
      trackingNumber,
      tracking_number: trackingNumber,
      _debug: {
        createdAt: new Date().toISOString(),
        payloadPreview: data?.description || null,
      },
    };
  },
};
