import { Controller, Get, Post, Body, Param, HttpCode, HttpStatus } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse } from '@nestjs/swagger';
import { SuppliesService } from './supplies.service';
import { CreateSupplyDto } from './dto/create-supply.dto';
import { SupplyResponseDto } from './dto/supply-response.dto';

@ApiTags('supplies')
@Controller('supplies')
export class SuppliesController {
  constructor(private readonly suppliesService: SuppliesService) {}

  @Get()
  @ApiOperation({ summary: 'Get all supplies' })
  @ApiResponse({ status: 200, description: 'List of supplies', type: [SupplyResponseDto] })
  findAll(): SupplyResponseDto[] {
    return this.suppliesService.findAll();
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get supply by ID' })
  @ApiResponse({ status: 200, description: 'Supply found', type: SupplyResponseDto })
  @ApiResponse({ status: 404, description: 'Supply not found' })
  findOne(@Param('id') id: string): SupplyResponseDto {
    return this.suppliesService.findOne(id);
  }

  @Post()
  @HttpCode(HttpStatus.CREATED)
  @ApiOperation({ summary: 'Create a new supply' })
  @ApiResponse({ status: 201, description: 'Supply created', type: SupplyResponseDto })
  create(@Body() createSupplyDto: CreateSupplyDto): SupplyResponseDto {
    return this.suppliesService.create(createSupplyDto);
  }
}
