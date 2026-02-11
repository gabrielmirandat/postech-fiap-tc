import { ApiProperty } from '@nestjs/swagger';

export class SupplyResponseDto {
  @ApiProperty({ description: 'Supply ID', example: '1234567890' })
  id: string;

  @ApiProperty({ description: 'Supply name', example: 'Laptop' })
  name: string;

  @ApiProperty({ description: 'Supply description', example: 'Dell XPS 15', required: false })
  description?: string;

  @ApiProperty({ description: 'Supply quantity', example: 10 })
  quantity: number;

  @ApiProperty({ description: 'Supply category', example: 'Electronics', required: false })
  category?: string;

  @ApiProperty({ description: 'Creation timestamp', example: '2026-02-10T21:00:00.000Z' })
  createdAt: string;
}
