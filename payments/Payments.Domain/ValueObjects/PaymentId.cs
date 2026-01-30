namespace Payments.Domain.ValueObjects;

public class PaymentId : IEquatable<PaymentId>
{
    public Guid Value { get; }

    private PaymentId(Guid value)
    {
        if (value == Guid.Empty)
            throw new ArgumentException("PaymentId cannot be empty", nameof(value));

        Value = value;
    }

    public static PaymentId Create() => new(Guid.NewGuid());

    public static PaymentId From(Guid value) => new(value);

    public static PaymentId From(string value)
    {
        if (string.IsNullOrWhiteSpace(value))
            throw new ArgumentException("PaymentId string cannot be empty", nameof(value));

        if (!Guid.TryParse(value, out var guid))
            throw new ArgumentException("Invalid PaymentId format", nameof(value));

        return new PaymentId(guid);
    }

    public bool Equals(PaymentId? other)
    {
        if (other is null) return false;
        if (ReferenceEquals(this, other)) return true;
        return Value == other.Value;
    }

    public override bool Equals(object? obj) => Equals(obj as PaymentId);

    public override int GetHashCode() => Value.GetHashCode();

    public override string ToString() => Value.ToString();

    public static implicit operator Guid(PaymentId id) => id.Value;
    public static implicit operator PaymentId(Guid value) => From(value);
}
