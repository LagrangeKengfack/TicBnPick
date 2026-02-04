"use client"

import React from "react"
import {
  ResponsiveContainer,
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
} from "recharts"

type Point = { month: string; earnings: number; lastYear?: number }

export function EarningsChart({ data }: { data: Point[] }) {
  return (
    <div style={{ width: "100%", height: 300 }}>
      <ResponsiveContainer>
        <LineChart data={data} margin={{ top: 10, right: 20, left: 0, bottom: 0 }}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="month" />
          <YAxis />
          <Tooltip />
          <Line type="monotone" dataKey="earnings" stroke="#f97316" strokeWidth={2} dot={{ r: 2 }} />
          {data.some((d) => d.lastYear !== undefined) && (
            <Line type="monotone" dataKey="lastYear" stroke="#06b6d4" strokeWidth={2} dot={false} strokeDasharray="4 4" />
          )}
        </LineChart>
      </ResponsiveContainer>
    </div>
  )
}

export default EarningsChart
