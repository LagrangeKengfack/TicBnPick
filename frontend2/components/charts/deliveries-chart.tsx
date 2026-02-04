"use client"

import React from "react"
import {
  ResponsiveContainer,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
} from "recharts"

type Point = { month: string; deliveries: number }

export function DeliveriesChart({ data }: { data: Point[] }) {
  return (
    <div style={{ width: "100%", height: 300 }}>
      <ResponsiveContainer>
        <BarChart data={data} margin={{ top: 10, right: 20, left: 0, bottom: 0 }}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="month" />
          <YAxis />
          <Tooltip />
          <Bar dataKey="deliveries" fill="#06b6d4" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}

export default DeliveriesChart
