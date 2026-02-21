"""Test runner that discovers and runs all unit tests."""
import unittest
import sys
import os

if __name__ == "__main__":
    loader = unittest.TestLoader()
    suite = loader.discover(
        os.path.dirname(__file__),
        pattern="test_*.py",
    )
    runner = unittest.TextTestRunner(verbosity=2)
    result = runner.run(suite)
    sys.exit(0 if result.wasSuccessful() else 1)
