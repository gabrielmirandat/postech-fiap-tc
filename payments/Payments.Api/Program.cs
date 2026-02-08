using Payments.Infrastructure.Configuration;
using Microsoft.AspNetCore;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace Payments.Api;

public class Program
{
    public static void Main(string[] args)
    {
        BuildWebHost(args).Run();
    }

    public static IWebHost BuildWebHost(string[] args) =>
        WebHost.CreateDefaultBuilder(args)
            .UseStartup<Startup>()
            .Build();
}

public class Startup
{
    public Startup(IConfiguration configuration)
    {
        Configuration = configuration;
    }

    public IConfiguration Configuration { get; }

    public void ConfigureServices(IServiceCollection services)
    {
        // Add MVC services (ASP.NET Core 2.2)
        services.AddMvcCore();

        // Add infrastructure
        services.AddInfrastructure(Configuration);
    }

    public void Configure(IApplicationBuilder app, IHostingEnvironment env)
    {
        // MVC routing (ASP.NET Core 2.2)
        app.UseMvc();
    }
}
