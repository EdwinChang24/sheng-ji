<script lang="ts">
    import WorksOn from "./sections/WorksOn.svelte";
    import Foss from "./sections/FOSS.svelte";
    import LevelUp from "./sections/LevelUp.svelte";
    import NextLevel from "./sections/NextLevel.svelte";
    import PossibleTrumps from "./sections/PossibleTrumps.svelte";
    import Teammates from "./sections/Teammates.svelte";
    import TrumpCalls from "./sections/TrumpCalls.svelte";
    
    let scroll = 0;
    let height: number;
</script>

<svelte:window bind:scrollY={scroll} bind:innerHeight={height} />

<LevelUp />
<TrumpCalls />
<PossibleTrumps />
<Teammates />
<WorksOn />
<Foss />
<NextLevel />
