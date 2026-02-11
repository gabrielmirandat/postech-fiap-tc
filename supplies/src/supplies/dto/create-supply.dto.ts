import { ApiProperty } from '@nestjs/swagger';
import { IsString, IsNotEmpty, IsNumber, IsOptional } from 'class-validator';

export class CreateSupplyDto {
  @ApiProperty({ description: 'Supply name', example: 'Laptop' })
  @IsString()
  @IsNotEmpty()
  name: string;

  @ApiProperty({ description: 'Supply description', example: 'Dell XPS 15', required: false })
  @IsString()
  @IsOptional()
  description?: string;

  @ApiProperty({ description: 'Supply quantity', example: 10 })
  @IsNumber()
  @IsNotEmpty()
  quantity: number;

  @ApiProperty({ description: 'Supply category', example: 'Electronics', required: false })
  @IsString()
  @IsOptional()
  category?: string;
}
