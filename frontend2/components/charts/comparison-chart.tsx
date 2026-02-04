"use client"

import React from "react"
import {
  ResponsiveContainer,
  ComposedChart,
  Bar,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
  Legend,
} from "recharts"

type Point = { month: string; earnings: number; lastYear?: number }

export function ComparisonChart({ data }: { data: Point[] }) {
  return (
    <div style={{ width: "100%", height: 300 }}>
      <ResponsiveContainer>
        <ComposedChart data={data} margin={{ top: 10, right: 20, left: 0, bottom: 0 }}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="month" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="earnings" name="This Year" barSize={18} fill="#f97316" />
          {data.some((d) => d.lastYear !== undefined) && (
            <Line type="monotone" dataKey="lastYear" name="Last Year" stroke="#06b6d4" strokeWidth={2} dot={false} />
          )}
        </ComposedChart>
      </ResponsiveContainer>
    </div>
  )
}

export default ComparisonChart
