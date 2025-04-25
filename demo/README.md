# Repsy Repository Server

A minimal Spring Boot application for deploying and downloading `.rep` packages with metadata.

---

## 🔧 Prerequisites

- Java 17  
- Docker & Docker Compose  
- Maven Wrapper (`./mvnw`)

---

## 🐳 Services (Docker Compose)

Create a `docker-compose.yml` in project root with:

```yaml
version: "3.8"
services:
  db:
    image: postgres:15
    environment:
      POSTGRES_DB: fullstack_demo_database
      POSTGRES_USER: fullstack_demo
      POSTGRES_PASSWORD: 123456
    ports:
      - "5432:5432"

  minio:
    image: minio/minio
    command: server /data
    environment:
      MINIO_ACCESS_KEY: minioadmin
      MINIO_SECRET_KEY: minioadmin
    ports:
      - "9000:9000"
      - "9001:9001"
```

Run all services:

```bash
docker-compose up -d
```

---

## 🚀 Quick Start

- **API:** `http://localhost:8080`  
- **MinIO Console:** `http://localhost:9001`  
  (user: `minioadmin` / pass: `minioadmin`)

---

## 🛠️ API Endpoints

### 1. Deploy Package

```
POST /{packageName}/{version}
```

- **Content-Type:** `multipart/form-data`  
- **Form fields:**
  - `package` — `.rep` file
  - `meta` — `.json` metadata  
- **Responses:**
  - `201 Created` — success  
  - `400 Bad Request` — invalid file types  
  - `409 Conflict` — package+version already exists  

### 2. Download File

```
GET /{packageName}/{version}/{fileName}
```

- **Responses:**
  - `200 OK` — returns file with  
    `Content-Disposition: attachment; filename="{fileName}"`
  - `404 Not Found` — if not present  

---

## 💾 Storage Modes

- **file-system** (default)  
  Uses local directory defined by `STORAGE_FS_ROOT_DIR`.  
- **object-storage**  
  Uses MinIO credentials:
  - `STORAGE_MINIO_URL`
  - `STORAGE_MINIO_ACCESS_KEY`
  - `STORAGE_MINIO_SECRET_KEY`
  - `STORAGE_MINIO_BUCKET`

---

## ⚙️ Configuration

Set via environment variables _or_ `application.properties`:

```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/fullstack_demo_database
SPRING_DATASOURCE_USERNAME=fullstack_demo
SPRING_DATASOURCE_PASSWORD=123456
STORAGE_STRATEGY=file-system
STORAGE_FS_ROOT_DIR=/data
# or for object-storage:
# STORAGE_STRATEGY=object-storage
# STORAGE_MINIO_URL=http://<host>:9000
# STORAGE_MINIO_ACCESS_KEY=minioadmin
# STORAGE_MINIO_SECRET_KEY=minioadmin
# STORAGE_MINIO_BUCKET=repsy
```

---

## 🏗️ Build & Run Locally

```bash
# 1) Package JAR
./mvnw clean package -DskipTests

# 2) Build Docker image
docker build -t repsy/repo-server:latest .

# 3) Run container (file-system mode)
docker run -d --name repsy-app \
  -p 8080:8080 \
  -e STORAGE_STRATEGY=file-system \
  -e STORAGE_FS_ROOT_DIR=/data \
  -v $(pwd)/storage-data:/data \
  repsy/repo-server:latest
```

Test with `curl`:

```bash
# Prepare test files
echo "hello" > test.rep
cat <<EOF > testMeta.json
{"name":"mypkg","version":"1.0.0","author":"Mert","dependencies":[]}
EOF

# Deploy
curl -i -X POST http://localhost:8080/mypkg/1.0.0 \
  -F "package=@test.rep" \
  -F "meta=@testMeta.json"

# Download
curl -i http://localhost:8080/mypkg/1.0.0/test.rep --output out.rep
cat out.rep  # "hello"
```

---

## 📦 Storage Libraries

Published to Repsy Maven repo:

- `com.fullstack:storage-file-system:0.0.1-SNAPSHOT`  
- `com.fullstack:storage-object-storage:0.0.1-SNAPSHOT`  

---

## 🙋 Delivered By

Mert Şen  
