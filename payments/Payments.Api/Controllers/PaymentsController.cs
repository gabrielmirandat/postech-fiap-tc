using MediatR;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Threading.Tasks;
using Payments.Application.DTOs;
using Payments.Application.UseCases.CreatePayment;
using Payments.Domain.ValueObjects;

namespace Payments.Api.Controllers;

[ApiController]
[Route("api/[controller]")]
public class PaymentsController : ControllerBase
{
    private readonly IMediator _mediator;
    private readonly ILogger<PaymentsController> _logger;

    public PaymentsController(IMediator mediator, ILogger<PaymentsController> logger)
    {
        _mediator = mediator;
        _logger = logger;
    }

    [HttpPost]
    public async Task<ActionResult<PaymentDto>> CreatePayment([FromBody] CreatePaymentCommand command)
    {
        try
        {
            var result = await _mediator.Send(command);
            _logger.LogInformation("Payment created: {PaymentId} for order {OrderId}", result.Id, result.OrderId);
            return CreatedAtAction(nameof(GetPayment), new { id = result.Id }, result);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error creating payment for order {OrderId}", command.OrderId);
            return BadRequest(new { error = ex.Message });
        }
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<PaymentDto>> GetPayment(Guid id)
    {
        // TODO: Implement GetPayment query handler
        return NotFound(new { error = "GetPayment query handler not implemented" });
    }
}
