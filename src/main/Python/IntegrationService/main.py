import logging
from contextlib import asynccontextmanager

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from API.integration_config_api import router as integration_config_router

# ── 日志配置 ──────────────────────────────────────────────
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(name)s - %(message)s",
)
logger = logging.getLogger("echoes.main")


# ── 生命周期钩子 ──────────────────────────────────────────
@asynccontextmanager
async def lifespan(app: FastAPI):
    logger.info("Echoes Python AI Service starting up...")
    yield
    logger.info("Echoes Python AI Service shutting down...")


# ── 应用实例 ──────────────────────────────────────────────
app = FastAPI(
    title="Echoes AI Integration Service",
    description="负责调用 LLM 生成信件，并将结果回调写回 Java 后端",
    version="1.0.0",
    lifespan=lifespan,
)

# ── CORS 中间件 ───────────────────────────────────────────
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],   # 生产环境请改为具体域名
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# ── 路由注册 ──────────────────────────────────────────────
app.include_router(integration_config_router, prefix="/api")


# ── 健康检查 ──────────────────────────────────────────────
@app.get("/health", tags=["System"])
async def health_check():
    return {"status": "ok", "service": "Echoes AI Integration Service"}


if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8001, reload=False)
