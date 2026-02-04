// Frontend-only simulated delivery service — no backend changes.
export async function acceptDelivery(deliveryId: string) {
  // simulate network latency
  await new Promise((r) => setTimeout(r, 300))
  return {
    success: true,
    id: deliveryId,
    // You can extend this return shape to match your backend if needed.
  }
}

export default { acceptDelivery }
