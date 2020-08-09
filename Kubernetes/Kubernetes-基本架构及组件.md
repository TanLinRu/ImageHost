## Kubernetes-基本架构与相关组件

* ### Kubernetes简介

* ### Kubernetes基础架构图

* ### Kubernetes组件解析



> #### Kubernetes简介

#### Kubernetes 是一个可移植的，可扩展的开源平台，用于**管理容器化**的工作负载和服务，方便了声明式配置和自动化。它拥有一个庞大且快速增长的生态系统。Kubernetes 的服务，支持和工具广泛可用。

![](https://d33wubrfki0l68.cloudfront.net/26a177ede4d7b032362289c6fccd448fc4a91174/eb693/images/docs/container_evolution.svg)

#### 对于K8S的架构以及K8S为什么会逐渐替代整个传统的问题，可以先从整个软件部署的整个架构演进进行分析与思考。

#### **传统部署时代：** 早期，组织在物理服务器上运行应用程序。无法为物理服务器中的应用程序定义资源边界，这会导致**资源分配**问题。例如，如果在物理服务器上运行多个应用程序，则可能会出现一个应用程序占用大部分资源的情况，结果可能导致其他应用程序的性能下降。一种解决方案是在不同的物理服务器上运行每个应用程序，但是由于资源利用不足而无法扩展，并且组织维护许多物理服务器的成本很高。

#### **虚拟化部署时代：** 作为解决方案，引入了**虚拟化**功能，它允许您在单个物理服务器的 CPU 上运行多个虚拟机（VM）。**虚拟化功能允许应用程序在 VM 之间隔离**，并提供安全级别，因为一个应用程序的信息不能被另一应用程序自由地访问。

#### 因为虚拟化可以轻松地添加或更新应用程序、降低硬件成本等等，所以**虚拟化可以更好地利用物理服务器中的资源，并可以实现更好的可伸缩性。**

#### 每个 VM 是一台完整的计算机，在虚拟化硬件之上运行所有组件，包括其自己的操作系统。

#### **容器部署时代：** 容器类似于 VM，但是它们**具有轻量级的隔离属性**，可以在应用程序之间共享操作系统（OS）。因此，容器被认为是轻量级的。容器与 VM 类似，具有自己的文件系统、CPU、内存、进程空间等。由于它们与基础架构分离，因此可以跨云和 OS 分发进行移植。

#### 容器因具有许多优势而变得流行起来。下面列出了容器的一些好处：

- #### 敏捷应用程序的创建和部署：与使用 VM 镜像相比，提高了容器镜像创建的简便性和效率。

- #### 持续开发、集成和部署：通过快速简单的回滚(由于镜像不可变性)，提供可靠且频繁的容器镜像构建和部署。

- #### 关注开发与运维的分离：在构建/发布时而不是在部署时创建应用程序容器镜像，从而将应用程序与基础架构分离。

- #### 可观察性不仅可以显示操作系统级别的信息和指标，还可以显示应用程序的运行状况和其他指标信号。

- #### 跨开发、测试和生产的环境一致性：在便携式计算机上与在云中相同地运行。

- #### 云和操作系统分发的可移植性：可在 Ubuntu、RHEL、CoreOS、本地、Google Kubernetes Engine 和其他任何地方运行。

- #### 以应用程序为中心的管理：提高抽象级别，从在虚拟硬件上运行 OS 到使用逻辑资源在 OS 上运行应用程序。

- #### 松散耦合、分布式、弹性、解放的微服务：应用程序被分解成较小的独立部分，并且可以动态部署和管理 - 而不是在一台大型单机上整体运行。

- #### 资源隔离：可预测的应用程序性能。

- #### 资源利用：高效率和高密度。

#### 正是云计算的兴起，将虚拟化成熟的发展了起来，并能够将底层资源最大化的按需使用。也是正是如此，容器化的时代也随之慢慢兴起，但用过docker的相关容器化技术的人都知道，对于程序的部署，小规模还行。在大规模的时候，那就真的脚忙手乱了。也正是这种原因，技术的更新也使得K8S的时代提前到来。

#### Kubernetes 为您提供：

- #### **服务发现和负载均衡**

  #### Kubernetes 可以使用 DNS 名称或自己的 IP 地址公开容器，如果到容器的流量很大，Kubernetes 可以负载均衡并分配网络流量，从而使部署稳定。

- #### **存储编排**

  #### Kubernetes 允许您自动挂载您选择的存储系统，例如本地存储、公共云提供商等。

- ### **自动部署和回滚**

  #### 您可以使用 Kubernetes 描述已部署容器的所需状态，它可以以受控的速率将实际状态更改为所需状态。例如，您可以自动化 Kubernetes 来为您的部署创建新容器，删除现有容器并将它们的所有资源用于新容器。

- #### **自动二进制打包**

  #### Kubernetes 允许您指定每个容器所需 CPU 和内存（RAM）。当容器指定了资源请求时，Kubernetes 可以做出更好的决策来管理容器的资源。

- #### **自我修复**

  #### Kubernetes 重新启动失败的容器、替换容器、杀死不响应用户定义的运行状况检查的容器，并且在准备好服务之前不将其通告给客户端。

- #### **密钥与配置管理**

  #### Kubernetes 允许您存储和管理敏感信息，例如密码、OAuth 令牌和 ssh 密钥。您可以在不重建容器镜像的情况下部署和更新密钥和应用程序配置，也无需在堆栈配置中暴露密钥。

#### K8S与PAAS(这部分待调研)

#### Kubernetes：

- #### Kubernetes 不限制支持的应用程序类型。Kubernetes 旨在支持极其多种多样的工作负载，包括无状态、有状态和数据处理工作负载。如果应用程序可以在容器中运行，那么它应该可以在 Kubernetes 上很好地运行。

- #### Kubernetes 不部署源代码，也不构建您的应用程序。持续集成(CI)、交付和部署（CI/CD）工作流取决于组织的文化和偏好以及技术要求。

- #### Kubernetes 不提供应用程序级别的服务作为内置服务，例如中间件（例如，消息中间件）、数据处理框架（例如，Spark）、数据库（例如，mysql）、缓存、集群存储系统（例如，Ceph）。这样的组件可以在 Kubernetes 上运行，并且/或者可以由运行在 Kubernetes 上的应用程序通过可移植机制（例如，[开放服务代理](https://openservicebrokerapi.org/)）来访问。

- #### Kubernetes 不指定日志记录、监视或警报解决方案。它提供了一些集成作为概念证明，并提供了收集和导出指标的机制。

- #### Kubernetes 不提供或不要求配置语言/系统（例如 jsonnet），它提供了声明性 API，该声明性 API 可以由任意形式的声明性规范所构成。

- #### Kubernetes 不提供也不采用任何全面的机器配置、维护、管理或自我修复系统。

- #### 此外，Kubernetes 不仅仅是一个编排系统，实际上它消除了编排的需要。编排的技术定义是执行已定义的工作流程：首先执行 A，然后执行 B，再执行 C。相比之下，Kubernetes 包含一组独立的、可组合的控制过程，这些过程连续地将当前状态驱动到所提供的所需状态。从 A 到 C 的方式无关紧要，也不需要集中控制，这使得系统更易于使用且功能更强大、健壮、弹性和可扩展性。

> ####  Kubernetes 组件

#### Kubernetes架构组件图

![](https://d33wubrfki0l68.cloudfront.net/7016517375d10c702489167e704dcb99e570df85/7bb53/images/docs/components-of-kubernetes.png)

#### 控制平面组件（Control Plane Components）

#### 控制平面的组件对集群做出全局决策(比如调度)，以及检测和响应集群事件（例如，当不满足部署的 `replicas` 字段时，启动新的 [pod](https://kubernetes.io/docs/concepts/workloads/pods/pod-overview/)）。

#### 控制平面组件可以在集群中的任何节点上运行。然而，为了简单起见，设置脚本通常会在同一个计算机上启动所有控制平面组件，并且不会在此计算机上运行用户容器。请参阅[构建高可用性集群](https://kubernetes.io/docs/admin/high-availability/)中对于多主机 VM 的设置示例。

#### kube-apiserver

#### 主节点上负责提供 Kubernetes API 服务的组件；它是 Kubernetes 控制面的前端。

#### kube-apiserver 在设计上考虑了水平扩缩的需要。 换言之，通过部署多个实例可以实现扩缩。 参见[构造高可用集群](https://kubernetes.io/docs/admin/high-availability/)。

#### etcd

#### etcd 是兼具一致性和高可用性的键值数据库，可以作为保存 Kubernetes 所有集群数据的后台数据库。

#### 您的 Kubernetes 集群的 etcd 数据库通常需要有个备份计划。要了解 etcd 更深层次的信息，请参考 [etcd 文档](https://etcd.io/docs)。

#### kube-scheduler

#### 主节点上的组件，该组件监视那些新创建的未指定运行节点的 Pod，并选择节点让 Pod 在上面运行。

#### 调度决策考虑的因素包括单个 Pod 和 Pod 集合的资源需求、硬件/软件/策略约束、亲和性和反亲和性规范、数据位置、工作负载间的干扰和最后时限。

#### kube-controller-manager

#### 在主节点上运行[控制器](https://kubernetes.io/docs/admin/kube-controller-manager/)的组件。

#### 从逻辑上讲，每个[控制器](https://kubernetes.io/docs/admin/kube-controller-manager/)都是一个单独的进程，但是为了降低复杂性，它们都被编译到同一个可执行文件，并在一个进程中运行。

#### 这些控制器包括:

- #### 节点控制器（Node Controller）: 负责在节点出现故障时进行通知和响应。

- #### 副本控制器（Replication Controller）: 负责为系统中的每个副本控制器对象维护正确数量的 Pod。

- #### 端点控制器（Endpoints Controller）: 填充端点(Endpoints)对象(即加入 Service 与 Pod)。

- #### 服务帐户和令牌控制器（Service Account & Token Controllers）: 为新的命名空间创建默认帐户和 API 访问令牌.

#### 云控制器管理器-(cloud-controller-manager)

#### [cloud-controller-manager](https://kubernetes.io/docs/tasks/administer-cluster/running-cloud-controller/) 运行与基础云提供商交互的控制器。cloud-controller-manager 二进制文件是 Kubernetes 1.6 版本中引入的 alpha 功能。

#### cloud-controller-manager 仅运行云提供商特定的控制器循环。您必须在 kube-controller-manager 中禁用这些控制器循环，您可以通过在启动 kube-controller-manager 时将 `--cloud-provider` 参数设置为 `external` 来禁用控制器循环。

#### cloud-controller-manager 允许云供应商的代码和 Kubernetes 代码彼此独立地发展。在以前的版本中，核心的 Kubernetes 代码依赖于特定云提供商的代码来实现功能。在将来的版本中，云供应商专有的代码应由云供应商自己维护，并与运行 Kubernetes 的云控制器管理器相关联。

#### 以下控制器具有云提供商依赖性:

- #### 节点控制器（Node Controller）: 用于检查云提供商以确定节点是否在云中停止响应后被删除

- #### 路由控制器（Route Controller）: 用于在底层云基础架构中设置路由

- #### 服务控制器（Service Controller）: 用于创建、更新和删除云提供商负载均衡器

- #### 数据卷控制器（Volume Controller）: 用于创建、附加和装载卷、并与云提供商进行交互以编排卷

## Node 组件

#### 节点组件在每个节点上运行，维护运行的 Pod 并提供 Kubernetes 运行环境。

#### kubelet

#### 一个在集群中每个节点上运行的代理。它保证容器都运行在 Pod 中。

#### kubelet 接收一组通过各类机制提供给它的 PodSpecs，确保这些 PodSpecs 中描述的容器处于运行状态且健康。kubelet 不会管理不是由 Kubernetes 创建的容器。

#### kube-proxy

#### [kube-proxy](https://kubernetes.io/docs/reference/command-line-tools-reference/kube-proxy/) 是集群中每个节点上运行的网络代理,实现 Kubernetes [Service](https://kubernetes.io/docs/concepts/services-networking/service/) 概念的一部分。

#### kube-proxy 维护节点上的网络规则。这些网络规则允许从集群内部或外部的网络会话与 Pod 进行网络通信。

#### 如果操作系统提供了数据包过滤层并可用的话，kube-proxy会通过它来实现网络规则。否则，kube-proxy 仅转发流量本身。

### 容器运行环境(Container Runtime)

#### 容器运行环境是负责运行容器的软件。

#### Kubernetes 支持多个容器运行环境: [Docker](http://www.docker.com/)、 [containerd](https://containerd.io/)、[cri-o](https://cri-o.io/)、 [rktlet](https://github.com/kubernetes-incubator/rktlet) 以及任何实现 [Kubernetes CRI (容器运行环境接口)](https://github.com/kubernetes/community/blob/master/contributors/devel/sig-node/container-runtime-interface.md)。

#### 插件(Addons)

#### 插件使用 Kubernetes 资源 ([DaemonSet](https://kubernetes.io/docs/concepts/workloads/controllers/daemonset), [Deployment](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/)等) 实现集群功能。因为这些提供集群级别的功能，所以插件的命名空间资源属于 `kube-system` 命名空间。

#### 所选的插件如下所述：有关可用插件的扩展列表，请参见[插件 (Addons)](https://kubernetes.io/docs/concepts/cluster-administration/addons/)。

#### DNS

#### 尽管并非严格要求其他附加组件，但所有示例都依赖[集群 DNS](https://kubernetes.io/docs/concepts/services-networking/dns-pod-service/)，因此所有 Kubernetes 集群都应具有 DNS。

#### 除了您环境中的其他 DNS 服务器之外，集群 DNS 还是一个 DNS 服务器，它为 Kubernetes 服务提供 DNS 记录。

#### Cluster DNS 是一个 DNS 服务器，和您部署环境中的其他 DNS 服务器一起工作，为 Kubernetes 服务提供DNS记录。

#### Kubernetes 启动的容器自动将 DNS 服务器包含在 DNS 搜索中。

#### 用户界面(Dashboard)

#### [Dashboard](https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/) 是 Kubernetes 集群的通用基于 Web 的 UI。它使用户可以管理集群中运行的应用程序以及集群本身并进行故障排除。

#### 容器资源监控

#### [容器资源监控](https://kubernetes.io/docs/tasks/debug-application-cluster/resource-usage-monitoring/)将关于容器的一些常见的时间序列度量值保存到一个集中的数据库中，并提供用于浏览这些数据的界面。

#### 集群层面日志

#### [集群层面日志](https://kubernetes.io/docs/concepts/cluster-administration/logging/) 机制负责将容器的日志数据保存到一个集中的日志存储中，该存储能够提供搜索和浏览接口。