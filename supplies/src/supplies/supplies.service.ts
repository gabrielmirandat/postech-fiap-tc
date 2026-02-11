import { Injectable, NotFoundException } from '@nestjs/common';
import { CreateSupplyDto } from './dto/create-supply.dto';
import { SupplyResponseDto } from './dto/supply-response.dto';

@Injectable()
export class SuppliesService {
  private supplies: SupplyResponseDto[] = [];

  findAll(): SupplyResponseDto[] {
    return this.supplies;
  }

  findOne(id: string): SupplyResponseDto {
    const supply = this.supplies.find(s => s.id === id);
    if (!supply) {
      throw new NotFoundException(`Supply with ID ${id} not found`);
    }
    return supply;
  }

  create(createSupplyDto: CreateSupplyDto): SupplyResponseDto {
    const supply: SupplyResponseDto = {
      id: Date.now().toString(),
      ...createSupplyDto,
      createdAt: new Date().toISOString(),
    };
    this.supplies.push(supply);
    return supply;
  }
}
