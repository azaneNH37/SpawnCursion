# Spawncursion
### Currently in dev QAQ
## Intro
A Minecraft mod aims for a more controllable and custom-designable "spawner" to 
allow users to generate a series of multi-type enemies with initial effects,
trace and control the generated enemies by their amounts, lifecycles and active areas, and introduce the generating process in 
natural features , boss-like enemies or world events.

## Feature
| ID | 名称     | 描述                           |
|----|--------|------------------------------|
| 0  | 刷怪笼模板  | 能通过`json`文件配置刷怪笼信息           |
| 1  | 复种类支持  | 在单刷怪笼内定义多种类型的待召唤实体           |
| 2  | 数量控制   | 能控制每种实体在单刷怪笼生命周期内应当生成/被击杀的数量 |
| 3  | 生物附加效果 | 能使通过本刷怪笼生成的实体获得属性加成，药水效果     |
| 5  | 生物行为附加 | 能修正实体的`aiGoal`               |
| 6  | 生成控制   | 能控制本刷怪笼的某种实体的刷怪间隔，刷怪范围       |
| 7  | 奖励箱    | 刷怪笼生命周期结束后，在刷怪笼中心生成奖励箱       |
| 8  | 子刷怪笼   | 能在刷怪笼被激活时生成子刷怪笼              |
| 9  | 全局数据包  | 游戏根目录`./spcurs`下可放置模组所需数据包   |

## TODO list

### High Priority
- [ ] **模板继承:** 应当能使某刷怪笼的配置继承一个父亲配置
- [ ] **错误检查:** 针对`json`文件配置提供检查和更详细的报错