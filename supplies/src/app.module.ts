import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { SuppliesModule } from './supplies/supplies.module';

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: '.env',
    }),
    SuppliesModule,
  ],
})
export class AppModule {}
