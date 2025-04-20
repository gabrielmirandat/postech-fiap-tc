import edgedb
import asyncio
from src.config import config as app_config

async def init_schema():
    """Initialize the EdgeDB schema if it doesn't exist."""
    try:
        print(f"Connecting to EdgeDB with DSN: {app_config.EDGEDB_DSN}")
        # For async operations, use create_async_client instead
        client = edgedb.create_async_client(dsn=app_config.EDGEDB_DSN)
        
        # Check if 'default' module exists
        try:
            print("Checking if schema exists...")
            result = await client.query_single('''
                SELECT EXISTS(
                    SELECT schema::Module 
                    FILTER .name = 'default'
                )
            ''')
            print(f"Schema check result: {result}")
        except Exception as e:
            print(f"Error checking schema: {e}")
            # Continue anyway to try creating schema
        
        # Create the Customer type unconditionally to ensure it exists
        try:
            print("Creating Customer schema...")
            await client.execute('''
                CREATE TYPE default::Customer {
                    CREATE REQUIRED PROPERTY govId -> str {
                        CREATE CONSTRAINT exclusive;
                    };
                    CREATE REQUIRED PROPERTY name -> str;
                    CREATE REQUIRED PROPERTY email -> str;
                };
            ''')
            print('Schema created successfully')
        except edgedb.errors.SchemaError as e:
            # If the type already exists, this will fail with a SchemaError
            if "already exists" in str(e):
                print("Customer schema already exists")
            else:
                print(f"Schema creation error: {e}")
        except Exception as e:
            print(f"Unexpected error creating schema: {e}")
            
    except Exception as e:
        print(f'Error initializing schema: {e}')
        print(f'Current DSN: {app_config.EDGEDB_DSN}')

# Allow direct execution for testing
if __name__ == "__main__":
    asyncio.run(init_schema()) 