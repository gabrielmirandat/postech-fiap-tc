FROM mcr.microsoft.com/dotnet/aspnet:10.0 AS base
WORKDIR /app
EXPOSE 80
EXPOSE 443

FROM mcr.microsoft.com/dotnet/sdk:10.0 AS build
WORKDIR /src

# Copy csproj files and restore dependencies
COPY ["Payments.Api/Payments.Api.csproj", "Payments.Api/"]
COPY ["Payments.Application/Payments.Application.csproj", "Payments.Application/"]
COPY ["Payments.Domain/Payments.Domain.csproj", "Payments.Domain/"]
COPY ["Payments.Infrastructure/Payments.Infrastructure.csproj", "Payments.Infrastructure/"]

RUN dotnet restore "Payments.Api/Payments.Api.csproj"

# Copy everything else and build
COPY . .
WORKDIR "/src/Payments.Api"
RUN dotnet build "Payments.Api.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "Payments.Api.csproj" -c Release -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "Payments.Api.dll"]
