export type Priority = 'LOW' | 'MEDIUM' | 'HIGH'
export type Status = 'OPEN' | 'IN_PROGRESS' | 'CLOSED'

export interface TicketDTO {
  id: number
  title: string
  description: string
  priority: Priority
  status: Status
  companyId: number
  customerId: number
  assignedUserId: number
  createdAt: string
  updatedAt: string
}

export interface TicketCreateDTO {
  title: string
  description: string
  priority: Priority
  status: Status
  companyId: number
  customerId: number
  assignedUserId: number
}

export interface TicketUpdateDTO {
  title: string
  description: string
  priority: Priority
  status: Status
  companyId: number
  customerId: number
  assignedUserId: number
}
