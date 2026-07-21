# SAIS Backend — Smart Agriculture Irrigation System

The backend service for **SAIS**, a full-stack platform that combines **IoT** and **AI** to automate farm irrigation. This Spring Boot service collects soil-moisture readings from field sensors over MQTT, pulls live weather forecasts, and uses an LLM-driven decision engine (with a rule-based fallback) to decide **when** and **how long** to irrigate each field.

🌐 **Live demo:** https://smartagricultureirrigation.app
🎨 **Frontend repo:** [`../sais-ui-vue3`](../sais-ui-vue3)

---

## ✨ What this service does

- **Crop & Field domain** — crop profiles with per-growth-stage moisture thresholds, planting plans, and field polygons
- **Sensor & Device management** — register soil-moisture sensors and irrigation devices per field; send start/stop commands
- **IoT Integration** — connects to **AWS IoT Core** over MQTT to receive sensor data and send irrigation commands (mock hardware in this project; real hardware uses the same message format)
- **Weather collection job** — polls [WeatherAPI.com](https://www.weatherapi.com/) every 10 minutes per farm
- **AI Irrigation Decision Engine** — every 30 minutes, a scheduled task reviews soil moisture, growth stage, and weather, then a **DeepSeek** LLM decides whether to irrigate, with a **rule-based engine** as fallback
- **Dry-run comparison** — both engines run on the same input in simulation mode; results are stored for evaluation
- **Irrigation plans** — records every generated plan; supports cancelling plans that haven't started or are running
- **Alerts** — abnormal sensors, extreme weather, irrigation problems, and crop water-risk alerts, each with a severity level and status lifecycle, plus in-app notifications
- **Demo / Developer panel API** — manually trigger the sensor and weather jobs to test the full IoT pipeline

---

## 🛠️ Tech Stack

| Technology | Version | Role |
| --- | --- | --- |
| Java + Spring Boot | 21 / 3.3.6 | Language & framework (REST API) |
| MyBatis Plus | 3.5.x | ORM over PostgreSQL |
| PostgreSQL | AWS RDS | Primary database |
| Redis | 6.x | Cache & distributed locks |
| AWS IoT Core | Managed | MQTT broker |
| DeepSeek API | — | LLM decision engine |
| WeatherAPI.com | — | Weather forecasts |
| Maven | — | Build (multi-module) |
| JUnit 5 + Mockito | — | Unit testing |

---

## 📁 Module Structure

```
sais-parent/
├── sais-server/         # Application entry point (runnable JAR)
├── sais-framework/      # Shared framework code
├── sais-module-agri/    # Core domain: crops, fields, sensors, irrigation,
│                        #   alerts, AI decision engine, weather jobs
├── sais-module-system/  # Users, auth, tenants
├── sais-module-infra/   # Infrastructure (file storage, jobs, etc.)
├── sais-dependencies/   # Dependency version management (BOM)
└── sql/postgresql/      # Database schema
```

The agriculture domain (`sais-module-agri`) is organised by feature under
`controller/admin/`: `crop`, `field`, `sensor`, `irrigation`, `alert`,
`evaluation`, `sensorreporting`, `farm`, and `demo`.

---

## 🏗️ Where the backend sits

The backend is the **service layer** of a four-layer system: the Vue frontend
talks to it over REST, and it talks to field hardware through **AWS IoT Core
(MQTT)**. It also holds the scheduled jobs, the DeepSeek decision engine, and
the PostgreSQL/Redis data tier.

![System Architecture](../docs/screenshots/architecture.png)

---

## 🚀 Getting Started

### Prerequisites

- **JDK 21** and **Maven 3.9+**
- **PostgreSQL** and **Redis** running locally
- Credentials for AWS IoT Core, DeepSeek, and WeatherAPI.com

### Run locally

```bash
# 1. Create the database and load the schema
psql -U postgres -f sql/postgresql/<schema>.sql

# 2. Configure credentials in the local profile:
#    sais-server/src/main/resources/application-local.yml
#    (DB, Redis, AWS IoT clientId/endpoint, DeepSeek key, WeatherAPI key)

# 3. Build and run
mvn clean package -DskipTests
java -jar sais-server/target/sais-server.jar --spring.profiles.active=local
```

> **Note:** API keys and connection secrets are **not** committed. Provide your
> own via `application-local.yml`.

### API spec

The full REST API is documented in [`../docs/agri_OpenAPI.json`](../docs/agri_OpenAPI.json) (OpenAPI 3).

---

## 🧪 Testing

Unit tests (JUnit 5 + Mockito) run without a Spring context, so they stay fast:

- **AI decision engine** — parsing the model's response and falling back to the rules when it fails
- **Rule-based engine** — soil-moisture threshold decisions across all cases
- **Decision pipeline** — driving soil moisture and rainfall through to a final decision, and checking the AI stays consistent with the rule-based baseline
- **Alert checks** — soil-moisture thresholds and extreme-weather alerts

---

## 📄 License

See [`LICENSE`](./LICENSE).

## 🙏 Acknowledgements

Built as a COMPX576 project at the University of Waikato by **Mingwen Xu**.
Scaffolded on the open-source [yudao-boot-mini](https://github.com/yudaocode/yudao-boot-mini).
