using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using Payments.Infrastructure.Configuration;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();
builder.Services.AddInfrastructure(builder.Configuration);

var app = builder.Build();

app.MapControllers();


app.Run();
