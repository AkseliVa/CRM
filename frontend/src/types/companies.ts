export type Industry = 'FINANCE' | 'MARKETING' | 'IT'

export interface CompanyDTO {
  id: number
  name: string
  industry: Industry
  createdAt: string
  updatedAt: string
}

export interface CompanyCreateDTO {
  name: string
  industry: Industry
}

export interface CompanyUpdateDTO {
  name: string
  industry: Industry
}
