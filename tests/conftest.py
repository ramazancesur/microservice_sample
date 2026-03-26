import sys
import os
import importlib.util


def load_service(service_dir: str, module_name: str):
    """Load a service's main module by path, registering it under a unique name."""
    main_path = os.path.join(
        os.path.dirname(__file__), "..", "services", service_dir, "main.py"
    )
    spec = importlib.util.spec_from_file_location(module_name, os.path.abspath(main_path))
    module = importlib.util.module_from_spec(spec)
    sys.modules[module_name] = module
    spec.loader.exec_module(module)
    return module

