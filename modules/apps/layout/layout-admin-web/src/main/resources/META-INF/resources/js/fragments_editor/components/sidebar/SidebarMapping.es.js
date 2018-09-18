import Component from 'metal-component';
import {Config} from 'metal-state';
import Soy from 'metal-soy';

import 'frontend-js-web/liferay/compat/modal/Modal.es';
import templates from './SidebarMapping.soy';
import {OPEN_ASSET_TYPE_DIALOG} from '../../actions/actions.es';
import {Store} from '../../store/store.es';

/**
 * SidebarMapping
 */

class SidebarMapping extends Component {

	/**
	 * @inheritDoc
	 * @review
	 */

	disposed() {
		if (this.highlightMapping) {
			this.emit('toggleHighlightMapping');
		}
	}

	/**
	 * Callback executed on highlight mapping checkbox click
	 * @private
	 * @review
	 */

	_handleHighlightMappingCheckboxChange() {
		this.emit('toggleHighlightMapping');
	}

	/**
	 * Open asset type selection dialog
	 * @private
	 * @review
	 */

	_handleSelectAssetTypeButtonClick() {
		this.store.dispatchAction(OPEN_ASSET_TYPE_DIALOG);
	}

}

/**
 * State definition.
 * @review
 * @static
 * @type {!Object}
 */

SidebarMapping.STATE = {

	/**
	 * If true, editable values should be highlighted.
	 * @default false
	 * @instance
	 * @memberOf FragmentsEditor
	 * @private
	 * @review
	 * @type {boolean}
	 */

	highlightMapping: Config.bool()
		.value(false),

	/**
	 * Selected mapping type label
	 * @default {}
	 * @instance
	 * @memberOf SidebarMapping
	 * @review
	 * @type {{
	 *   subtype: {
	 *   	id: !string,
	 *   	label: !string
	 *   },
	 *   type: {
	 *   	id: !string,
	 *   	label: !string
	 *   }
	 * }}
	 */

	selectedMappingTypes: Config
		.shapeOf(
			{
				subtype: Config.shapeOf(
					{
						id: Config.string().required(),
						label: Config.string().required()
					}
				),
				type: Config.shapeOf(
					{
						id: Config.string().required(),
						label: Config.string().required()
					}
				)
			}
		)
		.value({}),

	/**
	 * Store instance
	 * @default undefined
	 * @instance
	 * @memberOf FragmentEditableField
	 * @review
	 * @type {Store}
	 */

	store: Config.instanceOf(Store)

};

Soy.register(SidebarMapping, templates);

export {SidebarMapping};
export default SidebarMapping;