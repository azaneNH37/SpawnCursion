# Spawncursion
### 生成式入侵

## Intro
This mod completely rewrites and enhances the vanilla spawner mechanics in Minecraft, 
aiming to provide a highly controllable, freely customizable, and powerful monster-spawning system. 
Through lightweight scripting such as JSON configuration files and the support of global datapacks, 
the mod's functionality can be flexibly expanded and deeply customized. This allows for the introduction 
of custom challenges and trials in the overworld, serves as a new form of boss battles in generated 
structures, or enables the easy creation and management of complex combat scenarios and "campaigns."
<br>
本模组对《我的世界》原版刷怪笼功能进行了全面重写与强化，旨在提供一个高度可控、自由定制且功能强大的怪物生成系统。
通过JSON文件等轻代码内容编写和全局数据包的支持，本模组的功能得以灵活扩展与高度定制，使其能够在主世界中引入自定义的挑战与试炼、
作为结构生成中Boss战的全新表现形式，或用于便捷地创建和管理复杂的战斗场景与“战役”。

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