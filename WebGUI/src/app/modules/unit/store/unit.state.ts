import {UnitCacheState} from 'app/modules/unit/store/unit-cache/unit-cache.state';
import {UnitListState} from 'app/modules/unit/store/unit-list/unit-list.state';

export interface UnitState {
  unitListState: UnitListState;
  unitCacheState: UnitCacheState;
}
